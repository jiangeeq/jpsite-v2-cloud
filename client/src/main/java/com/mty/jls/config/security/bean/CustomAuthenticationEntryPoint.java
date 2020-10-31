package com.mty.jls.config.security.bean;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 暂时没有使用需要， 这里只是先记录学习，以便以后需求需要
 * 自定义认证异常处理类
 * @author jiangpeng
 * @date 2020/10/1514:15
 */
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.getWriter().write("login failed:" + authException.getMessage());
    }
}
