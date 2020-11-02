package com.mty.jls.config.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mty.jls.contract.constant.PropertiesConstant;
import com.mty.jls.contract.model.SecurityUser;
import com.dove.jls.common.utils.SpringUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.session.ConcurrentSessionControlAuthenticationStrategy;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author jiangpeng
 * @date 2020/10/1414:43
 */

public class CustomLoginFilter extends UsernamePasswordAuthenticationFilter {
    private SessionRegistry sessionRegistry;

    public CustomLoginFilter(String defaultFilterProcessesUrl, AuthenticationFailureHandler failureHandler, AuthenticationSuccessHandler successHandler,
                             AuthenticationManager manager, SessionRegistry sessionRegistry) {
        super.setAuthenticationFailureHandler(failureHandler);
        super.setAuthenticationSuccessHandler(successHandler);
        super.setAuthenticationManager(manager);
        super.setFilterProcessesUrl(defaultFilterProcessesUrl);

        ConcurrentSessionControlAuthenticationStrategy sessionStrategy = new ConcurrentSessionControlAuthenticationStrategy(sessionRegistry);
        sessionStrategy.setMaximumSessions(1);
        super.setSessionAuthenticationStrategy(sessionStrategy);

        this.sessionRegistry = sessionRegistry;
    }

    /**
     * 如果是通过 JSON 传递参数，则按照 JSON 的方式解析，如果不是，则调用 super.attemptAuthentication 方法
     * 也就是说，我们自定义的这个类，既支持 JSON 形式传递参数，也支持 key/value 形式传递参数。
     *
     * @param request
     * @param response
     * @return
     * @throws AuthenticationException
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        var isFrontBackendSeparation = "true".equals(SpringUtil.environment(PropertiesConstant.IS_FRONT_BACKEND_SEPARATION));
        if (isFrontBackendSeparation) {
            logger.info("****前后端分离模式，进入CustomLoginFilter");
            if (!request.getMethod().equals("POST")) {
                throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
            }
            Map<String, String> loginData = new HashMap<>(3);
            try {
                loginData = new ObjectMapper().readValue(request.getInputStream(), Map.class);
            } catch (IOException e) {
            } finally {
                loginData.put("code", loginData.get("code"));
            }
            String username = Optional.ofNullable(loginData.get(getUsernameParameter())).orElse("").trim();
            String password = Optional.ofNullable(loginData.get(getPasswordParameter())).orElse("").trim();

            UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);
            setDetails(request, authRequest);

            SecurityUser principal = new SecurityUser();
            principal.setUsername(username);
            sessionRegistry.registerNewSession(request.getSession(true).getId(), principal);
            return this.getAuthenticationManager().authenticate(authRequest);
        } else {
            return super.attemptAuthentication(request, response);
        }
    }
}
