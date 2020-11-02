package com.mty.jls.config.security.filter;

import cn.hutool.core.bean.BeanUtil;
import com.google.common.collect.Maps;
import com.mty.jls.contract.model.SecurityUser;
import com.dove.jls.common.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.springframework.boot.autoconfigure.security.oauth2.OAuth2ClientProperties;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2SsoProperties;
import org.springframework.context.ApplicationEvent;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2AuthenticationFailureEvent;
import org.springframework.security.oauth2.client.http.AccessTokenRequiredException;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author jiangpeng
 * @date 2020/10/1917:54
 */
@Slf4j
public class SsoJwtLoginFilter extends AbstractAuthenticationProcessingFilter {
    public static final Map<String, String> HEAD_CURRENT_JWT = Maps.newHashMap();

    private OAuth2SsoProperties oAuth2SsoProperties;
    private OAuth2ClientProperties oAuth2ClientProperties;
    private String accessTokenUri;
    private ResourceServerTokenServices tokenServices;

    private String accessTokenStr;

    public SsoJwtLoginFilter(String defaultFilterProcessesUrl, Environment environment,
                             AuthenticationFailureHandler failureHandler,
                             AuthenticationSuccessHandler successHandler,
                             OAuth2SsoProperties oAuth2SsoProperties,
                             OAuth2ClientProperties oAuth2ClientProperties,
                             AuthenticationManager authenticationManager, String accessTokenUri,
                             ResourceServerTokenServices tokenServices) {
        super(new AntPathRequestMatcher(oAuth2SsoProperties.getLoginPath()));
        super.setAuthenticationManager(authenticationManager);
        super.setAuthenticationSuccessHandler(successHandler);
        super.setAuthenticationFailureHandler(failureHandler);
        var isEnableOauth2Sso = "true".equals(environment.getProperty("is.enable.oauth2.sso"));

        if (!isEnableOauth2Sso) {
            // 没有开启单点登录则随便设置一个不存在的路径，这样就不会有请求执行 auth-server 认证调用
            super.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/isEnableOauth2Sso"));
        } else {
            super.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher(defaultFilterProcessesUrl));
        }

        this.oAuth2SsoProperties = oAuth2SsoProperties;
        this.oAuth2ClientProperties = oAuth2ClientProperties;
        this.accessTokenUri = accessTokenUri;
        this.tokenServices = tokenServices;
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException,
            IOException, ServletException {

        OAuth2AccessToken accessToken;
        try {
            final ResourceOwnerPasswordResourceDetails passwordResourceDetails = new ResourceOwnerPasswordResourceDetails();
            // 获取登录参数
            var securityUser = JsonUtil.decode(request.getInputStream(), SecurityUser.class);
            if (Objects.isNull(securityUser)) {
                var map = Map.of("username", request.getParameter("username"), "password", request.getParameter("password"));
                securityUser = JsonUtil.decode(JsonUtil.encode(map), SecurityUser.class);
            }

            passwordResourceDetails.setPassword(securityUser.getPassword());
            passwordResourceDetails.setUsername(securityUser.getUsername());
            passwordResourceDetails.setAccessTokenUri(accessTokenUri);
            passwordResourceDetails.setClientId(oAuth2ClientProperties.getClientId());
            passwordResourceDetails.setClientSecret(oAuth2ClientProperties.getClientSecret());
            passwordResourceDetails.setScope(List.of("all"));
            final OAuth2RestTemplate oAuth2RestTemplate = new OAuth2RestTemplate(passwordResourceDetails);
            accessToken = oAuth2RestTemplate.getAccessToken();
        } catch (OAuth2Exception e) {
            log.error("获取授权中心token失败[{}]", e.getMessage());
            BadCredentialsException bad = new BadCredentialsException("Could not obtain access token", e);
            publish(new OAuth2AuthenticationFailureEvent(bad));
            throw bad;
        }
        try {
            OAuth2Authentication result = tokenServices.loadAuthentication(accessToken.getValue());
            if (authenticationDetailsSource != null) {
                request.setAttribute(OAuth2AuthenticationDetails.ACCESS_TOKEN_VALUE, accessToken.getValue());
                request.setAttribute(OAuth2AuthenticationDetails.ACCESS_TOKEN_TYPE, accessToken.getTokenType());
                result.setDetails(authenticationDetailsSource.buildDetails(request));
            }
            accessTokenStr = accessToken.getValue();
            publish(new AuthenticationSuccessEvent(result));
            return result;
        } catch (InvalidTokenException e) {
            BadCredentialsException bad = new BadCredentialsException("Could not obtain user details from token", e);
            publish(new OAuth2AuthenticationFailureEvent(bad));
            throw bad;
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult) throws IOException, ServletException {
        // 在 filterChain.doFilter 之前设置cookie，否则无法生效
        Cookie cookie = new Cookie("token", accessTokenStr);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);

        // todo 为了测试才使用的，因为前后端分离的时候，所有的请求都不是页面传递过来，无法设置header，就把token保存到用户线程中
        HEAD_CURRENT_JWT.put(HttpHeaders.AUTHORIZATION, accessTokenStr);

        log.debug("使用oauth2Sso单点登陆，设置cookie token：[{}] 结果：[{}]", accessTokenStr, response.isCommitted());

        final Authentication userAuthentication = ((OAuth2Authentication) authResult).getUserAuthentication();

        SecurityUser securityUser = null;
        try {
            securityUser = BeanUtil.mapToBean((Map<String, Object>) userAuthentication.getDetails(), SecurityUser.class, false);
            securityUser.setAuthorities(userAuthentication.getAuthorities());
        } catch (Exception e) {
            e.printStackTrace();
        }
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(securityUser, null,
                userAuthentication.getAuthorities());

        super.successfulAuthentication(request, response, chain, authenticationToken);

    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {
        if (failed instanceof AccessTokenRequiredException) {
            throw failed;
        } else {
            super.unsuccessfulAuthentication(request, response, failed);
        }
    }

    private void publish(ApplicationEvent event) {
        if (eventPublisher != null) {
            eventPublisher.publishEvent(event);
        }
    }
}
