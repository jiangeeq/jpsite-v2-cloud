package com.mty.jls.config.security.bean;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

/**
 * @author jiangpeng
 * @date 2020/10/1418:00
 */
@Getter
@Setter
@Accessors(chain = true)
public class CustomWebAuthenticationDetails extends WebAuthenticationDetails {
    private String sessionId;
    private String contextPath;
    private String queryString;
    private String requestURI;
    private  String requestURL;
    private String  remoteAddr;
    private  Integer remotePort;
    private String  schema;
    private String  method;
    private  String characterEncoding;
    private  String contentType;
    private Locale local;

    /**
     * Records the remote address and will also set the session Id if a session already
     * exists (it won't create one).
     *
     * @param request that the authentication request was received from
     */
    public CustomWebAuthenticationDetails(HttpServletRequest request) {
        super(request);
    }
}
