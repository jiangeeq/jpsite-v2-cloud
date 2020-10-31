package com.mty.authserver.config.security;

import com.mty.authserver.config.security.filter.JwtFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.expression.OAuth2WebSecurityExpressionHandler;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

/**
 * @author jiangpeng
 * @date 2020/10/1513:05
 */
@Configuration
@Order(1)
@Slf4j
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Value("${jwt.sign.key}")
    private String jwtSignKey;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserDetailsService rbacUserDetailsService;
    @Autowired
    TokenStore tokenStore;


    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/login.html", "/css/**", "/js/**", "/images/**");
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        final OAuth2WebSecurityExpressionHandler oAuth2WebSecurityExpressionHandler = new OAuth2WebSecurityExpressionHandler();
        http.authorizeRequests()
                .antMatchers(HttpMethod.GET, "/hello").permitAll()
                .antMatchers("/login").permitAll()
                .antMatchers("/oauth/**").permitAll()
                .anyRequest().authenticated()
                .expressionHandler(oAuth2WebSecurityExpressionHandler)
                .and()
                .formLogin()
                .loginPage("/login.html")
                .loginProcessingUrl("/doLogin")
                .successHandler(((request, response, authentication) -> {
                    log.info("用户登陆成功");
                    response.sendRedirect("/hello");
                }))
                .failureHandler((request, response, exception) -> {
                    log.info("用户认证失败");
                    response.getWriter().println(exception.getMessage());
                })

                .and()
                .addFilterBefore(new JwtFilter(authenticationManager(), jwtSignKey),
                        BasicAuthenticationFilter.class)
                .userDetailsService(rbacUserDetailsService)
                // 跨域支持
                .csrf().disable().cors();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("sang")
                .password(passwordEncoder.encode("123"))
                .roles("admin");
    }

    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        DaoAuthenticationProvider dao1 = new DaoAuthenticationProvider();
        dao1.setUserDetailsService(rbacUserDetailsService);
        dao1.setPasswordEncoder(passwordEncoder);

        return new ProviderManager(dao1);
    }

}
