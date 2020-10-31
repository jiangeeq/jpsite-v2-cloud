package com.mty.jls.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mty.jls.config.security.bean.CustomWebAuthenticationDetailsSource;
import com.mty.jls.config.security.filter.CustomLoginFilter;
import com.mty.jls.config.security.filter.JwtFilter;
import com.mty.jls.config.security.filter.JwtLoginFilter;
import com.mty.jls.config.security.filter.SsoJwtLoginFilter;
import com.mty.jls.contract.annotations.Oauth2SsoSelector;
import com.mty.jls.contract.model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.OAuth2ClientProperties;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2SsoProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.session.ConcurrentSessionFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.session.security.SpringSessionBackedSessionRegistry;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * @author jiangpeng
 * @date 2020/10/1310:19
 */
@Import({Oauth2SsoSelector.class})
@Configuration
@EnableConfigurationProperties(OAuth2SsoProperties.class)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Value("${is.front.backend.separation}")
    private boolean isFrontBackendSeparation;
    @Value("${security.oauth2.client.access-token-uri}")
    private String accessTokenUri;
    @Value("${is.enable.csrf}")
    private boolean isEnableCsrf;

    @Autowired
    private Environment environment;
    @Autowired
    private JdbcTokenRepositoryImpl jdbcTokenRepository;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private AuthenticationFailureHandler authenticationFailureHandler;
    @Autowired
    private AuthenticationSuccessHandler authenticationSuccessHandler;
    @Autowired
    private CustomWebAuthenticationDetailsSource customWebAuthenticationDetailsSource;
//    @Autowired
//    private SessionRegistryImpl sessionRegistry;
    @Autowired
    private SpringSessionBackedSessionRegistry redisSessionRegistry;

    // 配置了security.oauth2.resource.user-info-uri 则使用UserInfoTokenServices 否则 TokenInfoServices
    @Autowired(required = false)
    private ResourceServerTokenServices tokenServices;
    @Autowired
    private ResourceServerProperties resourceServerProperties;
    @Autowired
    private OAuth2SsoProperties oAuth2SsoProperties;
    @Autowired
    private OAuth2ClientProperties oAuth2ClientProperties;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public void configure(WebSecurity web) throws Exception {
        // web.ignoring() 用来配置忽略掉的 URL 地址，一般对于静态文件，我们可以采用此操作。
        // tip 把登录请求地址放进来了，那就 gg 了, 请求将不走 SecurityContextPersistenceFilter 过滤器，也就意味着不会将登录用户信息存入 session，进而导致后续请求无法获取到登录用户信息
        web.ignoring().antMatchers("/js/**", "/css/**", "/images/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/log**", "/error", "/code/**").permitAll()
                .antMatchers("/swagger-resources",
                        "/v2/api-docs",
                        "/v2/api-docs-ext",
                        "/doc.html").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                // 自定义的 MyWebAuthenticationDetailsSource 代替系统默认的 WebAuthenticationDetailsSource, 即可成功使用我们自定义的 WebAuthenticationDetails
                .authenticationDetailsSource(customWebAuthenticationDetailsSource)
                .loginPage("/login.html")
                .loginProcessingUrl("/doLogin")
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))
                .logoutSuccessHandler((req, resp, authentication) -> {
                    if (isFrontBackendSeparation) {
                        resp.setContentType("application/json;charset=utf-8");
                        PrintWriter out = resp.getWriter();
                        out.write("注销成功");
                        out.flush();
                        out.close();
                    } else {
                        resp.sendRedirect("/login.html");
                    }
                })
                .deleteCookies()
                // clearAuthentication 和 invalidateHttpSession 分别表示清除认证信息和使 HttpSession 失效，默认可以不用配置，默认就会清除。
                .clearAuthentication(true)
                .invalidateHttpSession(true)
                .and()
                .rememberMe()
                // key 默认值是一个 UUID 字符串，这样会带来一个问题，就是如果服务端重启，这个 key 会变，这样就导致之前派发出去的所有 remember-me 自动登录令牌失效
                .key("javaboy")
                .tokenRepository(jdbcTokenRepository)
                .and()
                .addFilterBefore(new JwtFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JwtLoginFilter("/doLogin", authenticationManager), UsernamePasswordAuthenticationFilter.class)
                .addFilterAt(new CustomLoginFilter("/doLogin", authenticationFailureHandler, authenticationSuccessHandler, authenticationManager,
                                redisSessionRegistry),
                        UsernamePasswordAuthenticationFilter.class)
                .addFilterAt(new ConcurrentSessionFilter(redisSessionRegistry, event -> {
                    HttpServletResponse resp = event.getResponse();
                    resp.setContentType("application/json;charset=utf-8");
                    resp.setStatus(401);
                    PrintWriter out = resp.getWriter();
                    out.write(new ObjectMapper().writeValueAsString(Response.fail("您已在另一台设备登录，本次登录已下线!")));
                    out.flush();
                    out.close();
                }), ConcurrentSessionFilter.class)
                .addFilterAfter(new SsoJwtLoginFilter("/doLogin", environment, authenticationFailureHandler, authenticationSuccessHandler, oAuth2SsoProperties,
                                oAuth2ClientProperties,
                                authenticationManager, accessTokenUri,
                                tokenServices),
                        LogoutFilter.class)
                .csrf().disable()
                .userDetailsService(userDetailsService)
                .exceptionHandling()
                // 没有认证就访问数据的处理方案
                .authenticationEntryPoint((req, resp, authException) -> {
                            if (isFrontBackendSeparation) {
                                resp.setContentType("application/json;charset=utf-8");
                                PrintWriter out = resp.getWriter();
                                out.write("尚未登录，请先登录 <a href='http://localhost/login.html'></a>");
                                out.flush();
                                out.close();
                            } else {
                                resp.sendRedirect("/login.html");
                            }
                        }
                ).and()
                // 用新的登录踢掉旧的登录，我们只需要将最大会话数设置为 1
                .sessionManagement()
                .maximumSessions(1)
                .sessionRegistry(redisSessionRegistry);

        if (isEnableCsrf) {
            http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
        }
    }
}
