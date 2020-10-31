package com.mty.jls.config.security.bean;

import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @author jiangpeng
 * @date 2020/10/1418:03
 */
@Component
public class CustomWebAuthenticationDetailsSource implements AuthenticationDetailsSource<HttpServletRequest,CustomWebAuthenticationDetails> {
    @Override
    public CustomWebAuthenticationDetails buildDetails(HttpServletRequest req) {
        final CustomWebAuthenticationDetails details = new CustomWebAuthenticationDetails(req);
        details.setRemoteAddr(req.getRemoteAddr()).setContextPath(req.getContextPath()).setQueryString(req.getQueryString())
                .setRequestURI(req.getRequestURI()).setRequestURL(req.getRequestURL().toString()).setSessionId(req.getSession().getId())
                .setRemotePort(req.getRemotePort()).setSchema(req.getScheme()).setMethod(req.getMethod())
                .setCharacterEncoding(req.getCharacterEncoding()).setContentType(req.getContentType()).setLocal(req.getLocale());
        return details;
    }
}
