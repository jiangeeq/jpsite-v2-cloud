package com.mty.authserver.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;

/**
 * @author jiangpeng
 * @date 2020/10/1513:01
 */
@Configuration
// 开启授权服务器的自动化配置。
@EnableAuthorizationServer
public class AuthServerConfig extends AuthorizationServerConfigurerAdapter {
    @Autowired
    private UserDetailsService rbacUserDetailsService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthorizationServerTokenServices tokenServices;

    /**
     * 用来配置令牌端点的安全约束，也就是这个端点谁能访问，谁不能访问。
     *
     * @param security
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        // 允许所有token 访问
        security.tokenKeyAccess("permitAll()")
                // 允许check_token访问
                .checkTokenAccess("permitAll()")
                // 允许表单认证
                .allowFormAuthenticationForClients();
    }

    /**
     * 用来配置客户端的详细信息
     *
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient("javaboy")
                .secret(passwordEncoder.encode("123"))
                .autoApprove(true)
                .redirectUris("http://localhost:1111/login.html")
                .scopes("all")
                .accessTokenValiditySeconds(10)
                .refreshTokenValiditySeconds(10)
                .authorities("ROLE_ADMIN","ROLE_USER","ROLE_JLS")
                .authorizedGrantTypes("password", "client_credentials", "refresh_token", "authorization_code");

    }

    /**
     * 用来配置令牌的访问端点和令牌服务。
     *
     * @param endpoints
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        // 由于使用了 password 模式之后，用户要进行登录，所以我们需要配置一个 AuthenticationManager
        endpoints
                .authenticationManager(authenticationManager)
                .tokenServices(tokenServices)
                // 有权限访问的用户
                .userDetailsService(rbacUserDetailsService);
        ;
    }
}
