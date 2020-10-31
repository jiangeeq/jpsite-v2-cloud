package com.mty.jls.config.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mty.jls.contract.constant.PropertiesConstant;
import com.mty.jls.contract.model.SecurityUser;
import com.mty.jls.dovecommon.utils.JwtUtil;
import com.mty.jls.dovecommon.utils.SpringUtil;
import com.mty.jls.dovecommon.utils.ValidateUtil;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.session.NullAuthenticatedSessionStrategy;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author jiangpeng
 * @date 2020/10/1312:34
 */
public class JwtLoginFilter extends AbstractAuthenticationProcessingFilter {

    public JwtLoginFilter(String defaultFilterProcessesUrl, AuthenticationManager authenticationManager) {
        super(new AntPathRequestMatcher(defaultFilterProcessesUrl));
        setAuthenticationManager(authenticationManager);
    }


    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        var isEnableLoginJwt = "true".equals(SpringUtil.environment(PropertiesConstant.IS_ENABLE_LOGIN_JWT));

        if (!requiresAuthentication(request, response) || !isEnableLoginJwt) {
            ValidateUtil.validateExecute(!isEnableLoginJwt, log -> logger.debug("未开启JWT登录模式，跳过JwtLoginFilter"));
            chain.doFilter(request, response);
            return;
        }


        Authentication authResult;

        try {
            authResult = attemptAuthentication(request, response);
            if (authResult == null) {
                // return immediately as subclass has indicated that it hasn't completed
                // authentication
                return;
            }
            var sessionStrategy = new NullAuthenticatedSessionStrategy();
            sessionStrategy.onAuthentication(authResult, request, response);
        } catch (InternalAuthenticationServiceException failed) {
            logger.error(
                    "An internal error occurred while trying to authenticate the user.",
                    failed);
            unsuccessfulAuthentication(request, response, failed);
            return;
        } catch (AuthenticationException failed) {
            unsuccessfulAuthentication(request, response, failed);
            return;
        }

        successfulAuthentication(request, response, chain, authResult);
    }

    /**
     * 从登录参数中提取出用户名密码，然后调用AuthenticationManager.authenticate()方法去进行自动校验。
     *
     * @param request
     * @param response
     * @return
     * @throws AuthenticationException
     * @throws IOException
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException,
            IOException {
        var objectMapper = new ObjectMapper();
        SecurityUser securityUser;
        var isFrontBackendSeparation = "true".equals(SpringUtil.environment(PropertiesConstant.IS_FRONT_BACKEND_SEPARATION));
        // 前后端分离模式使用json传递参数
        if (isFrontBackendSeparation) {
            securityUser = objectMapper.readValue(request.getInputStream(), SecurityUser.class);
        } else {
            var map = Map.of("username", request.getParameter("username"), "password", request.getParameter("password"));
            var jsonReq = objectMapper.writeValueAsString(map);

            securityUser = objectMapper.readValue(jsonReq, SecurityUser.class);
        }

        var authenticationToken =
                new UsernamePasswordAuthenticationToken(securityUser.getUsername(), securityUser.getPassword());
        return getAuthenticationManager().authenticate(authenticationToken);

    }

    /**
     * 校验成功
     * 将用户角色遍历然后用一个 , 连接起来，然后再利用Jwts去生成token，按照代码的顺序，
     * 生成过程一共配置了四个参数，分别是用户角色、主题、过期时间以及加密算法和密钥，然后将生成的token写出到客户端。
     *
     * @param req
     * @param resp
     * @param chain
     * @param authResult
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse resp, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        String isFrontBackendSeparation = SpringUtil.environment(PropertiesConstant.IS_FRONT_BACKEND_SEPARATION);

        if ("true".equals(isFrontBackendSeparation)) {
            Collection<? extends GrantedAuthority> authorities = authResult.getAuthorities();
            var authoritiesStr = authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));

            String jwt = JwtUtil.encode(authResult.getPrincipal().toString()
                    , SpringUtil.environment(PropertiesConstant.JWT_SIGN_KEY)
                    , Map.of("authorities", authoritiesStr));

            Cookie cookie = new Cookie("token", jwt);
            cookie.setHttpOnly(true);
            resp.addCookie(cookie);
        } else {
            List<GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_admin");

            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(req.getParameter("username"), req.getParameter(
                    "password"), authorities);
            SecurityContextHolder.getContext().setAuthentication(token);
            resp.sendRedirect("/index.html");
        }
    }

    /**
     * 如果校验失败就会来到unsuccessfulAuthentication方法中
     *
     * @param req
     * @param resp
     * @param failed
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest req, HttpServletResponse resp, AuthenticationException failed) throws IOException,
            ServletException {
        String isFrontBackendSeparation = SpringUtil.environment(PropertiesConstant.IS_FRONT_BACKEND_SEPARATION);

        if ("true".equals(isFrontBackendSeparation)) {
            resp.setContentType("application/json;charset=utf-8");
            PrintWriter out = resp.getWriter();
            out.write("登录失败!");
            out.flush();
            out.close();
        } else {
            resp.sendRedirect("/login_error.html");
        }
    }
}
