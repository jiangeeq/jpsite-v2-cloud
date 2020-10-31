package com.mty.authserver.config.security.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetailsSource;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.DelegatingAuthenticationEntryPoint;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.util.matcher.RequestHeaderRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * @author jiangpeng
 * @date 2020/10/1312:34
 */
@Slf4j
public class JwtFilter extends BasicAuthenticationFilter {
    private final String jwtSignKey;
    private final AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource;

    private static final RequestHeaderRequestMatcher X_REQUESTED_WITH = new RequestHeaderRequestMatcher("X-Requested-With",
            "XMLHttpRequest");
    private static final AuthenticationEntryPoint authenticationEntryPoint;
    private static final LinkedHashMap<RequestMatcher, AuthenticationEntryPoint> entryPoints = new LinkedHashMap<>();

    static {
        entryPoints.put(X_REQUESTED_WITH, new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));
        DelegatingAuthenticationEntryPoint defaultEntryPoint = new DelegatingAuthenticationEntryPoint(
                entryPoints);
        BasicAuthenticationEntryPoint basicAuthEntryPoint = new BasicAuthenticationEntryPoint();

        defaultEntryPoint.setDefaultEntryPoint(basicAuthEntryPoint);
        authenticationEntryPoint = defaultEntryPoint;

    }

    public JwtFilter(AuthenticationManager authenticationManager,  String jwtSignKey) {
        super(authenticationManager, authenticationEntryPoint);
        this.authenticationDetailsSource = new OAuth2AuthenticationDetailsSource();
        super.setAuthenticationDetailsSource(authenticationDetailsSource);
        this.jwtSignKey = jwtSignKey;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        try {
            UsernamePasswordAuthenticationToken authRequest = convertJwt(request);
            if (authRequest == null) {
                super.doFilter(request, response, chain);
                return;
            }

            String username = authRequest.getName();

            if (authenticationIsRequired(username)) {
                SecurityContextHolder.getContext().setAuthentication(authRequest);
                onSuccessfulAuthentication(request, response, authRequest);
            }
        } catch (AuthenticationException failed) {
            log.debug("Authentication request for failed!", failed);
            super.doFilterInternal(request, response, chain);
        }
        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken convertJwt(HttpServletRequest request) {
        final HashMap<String, Object> map = new HashMap<>();
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (Strings.isBlank(header)) {
            return null;
        }
        String jwt = header.replace("Bearer ", "").replace("bearer ", "");
        // 不使用base64编码签名
        Claims claims = Jwts.parser().setSigningKey(jwtSignKey.getBytes()).parseClaimsJws(jwt).getBody();
        if (Objects.isNull(claims)) {
            return null;
        }
        var username = claims.get("user_name");
        var authoritiesList = (ArrayList<String>) claims.get("authorities");
        List<GrantedAuthority> authorities =
                AuthorityUtils.commaSeparatedStringToAuthorityList(authoritiesList.stream().map(String::toString).collect(Collectors.joining(",")));
        // 创建一个UsernamePasswordAuthenticationToken放到当前的Context中，然后执行过滤链使请求继续执行下去。
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, null, authorities);
        token.setDetails(this.authenticationDetailsSource.buildDetails(request));
        return token;
    }

    private boolean authenticationIsRequired(String username) {
        Authentication existingAuth = SecurityContextHolder.getContext()
                .getAuthentication();
        if (existingAuth == null || !existingAuth.isAuthenticated()) {
            return true;
        }
        if (existingAuth instanceof UsernamePasswordAuthenticationToken
                && !existingAuth.getName().equals(username)) {
            return true;
        }
        if (existingAuth instanceof AnonymousAuthenticationToken) {
            return true;
        }
        return false;
    }
}
