package com.mty.jls.config.security.filter;

import com.mty.jls.contract.constant.PropertiesConstant;
import com.mty.jls.dovecommon.utils.JsonUtil;
import com.mty.jls.dovecommon.utils.JwtUtil;
import com.mty.jls.dovecommon.utils.SpringUtil;
import com.mty.jls.dovecommon.utils.ValidateUtil;
import com.mty.jls.rbac.service.impl.RbacUserDetailsService;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.apache.logging.log4j.util.Strings;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;

import javax.servlet.FilterChain;
import javax.servlet.GenericFilter;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author jiangpeng
 * @date 2020/10/1312:44
 */
@Slf4j
public class JwtFilter extends GenericFilter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;

        if (SecurityContextHolder.getContext().getAuthentication() != null){
            log.debug("已经登录成功，跳过JwtFilter");
            chain.doFilter(request,response);
            return;
        }

        var isFrontBackendSeparation = "true".equals(SpringUtil.environment(PropertiesConstant.IS_FRONT_BACKEND_SEPARATION));
        var isEnableLoginJwt = "true".equals(SpringUtil.environment(PropertiesConstant.IS_ENABLE_LOGIN_JWT));
        var isEnableOauth2Sso = "true".equals(SpringUtil.environment(PropertiesConstant.IS_ENABLE_OAUTH2_SSO));

        if (isFrontBackendSeparation && isEnableLoginJwt) {
            // 首先从请求头中提取出 authorization 字段，这个字段对应的value就是用户的token。
            String jwtToken = req.getHeader(HttpHeaders.AUTHORIZATION);
            // todo 如果无法从head获取到，表示是本地接口请求测试，则从用户线程中获取toeken方便测试。
            if (Strings.isBlank(jwtToken) && true) {
                jwtToken = SsoJwtLoginFilter.HEAD_CURRENT_JWT.get(HttpHeaders.AUTHORIZATION);
            }

            log.info("jwtToken is {}", jwtToken);
            if (Strings.isNotBlank(jwtToken)) {
                if(isEnableOauth2Sso) {
                    final Jwt decode = JwtHelper.decode(jwtToken);
                    var map = JsonUtil.toMap(decode.getClaims(), JsonUtil.StringObjectMap);

                    var expireTime = LocalDateTime.ofEpochSecond(Long.parseLong(map.get("exp").toString()), 0, ZoneOffset.ofHours(8));
                    ValidateUtil.validateForResponse(LocalDateTime.now().isAfter(expireTime),"token已过期，请重新登录");
                    var username = (String)map.get("user_name");
                    List<String> authorities = (List<String>) map.get("authorities");
                    final List<GrantedAuthority> grantedAuthorities =
                            AuthorityUtils.commaSeparatedStringToAuthorityList(authorities.stream().collect(Collectors.joining(",")));
                    final RbacUserDetailsService rbacUserDetailsService = SpringUtil.getBean(RbacUserDetailsService.class);

                    final UserDetails userDetails = rbacUserDetailsService.loadUserByUsername(username);
                    UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails, null, grantedAuthorities);
                    SecurityContextHolder.getContext().setAuthentication(token);
                }else{
                    // 提取出来的token字符串转换为一个Claims对象，再从Claims对象中提取出当前用户名和用户角色
                    Claims claims = JwtUtil.decode(jwtToken, SpringUtil.environment(PropertiesConstant.JWT_SIGN_KEY));
                    String username = claims.getSubject();
                    List<GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList((String) claims.get("authorities"));
                    // 创建一个UsernamePasswordAuthenticationToken放到当前的Context中，然后执行过滤链使请求继续执行下去。
                    UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, null, authorities);
                    SecurityContextHolder.getContext().setAuthentication(token);
                }
            }
        }
        chain.doFilter(req, response);
    }
}
