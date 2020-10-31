package com.mty.jls.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mty.jls.code.ValidateCodeProcessorHolder;
import com.mty.jls.config.security.provider.MyAuthenticationProvider;
import com.mty.jls.contract.constant.PropertiesConstant;
import com.mty.jls.contract.model.Response;
import com.mty.jls.dovecommon.utils.SpringUtil;
import com.mty.jls.properties.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.security.SpringSessionBackedSessionRegistry;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.util.Arrays;

/**
 * @author jiangpeng
 * @date 2020/10/1416:16
 */
@Configuration
public class SecurityBeanConfig {

    @Bean
    JdbcTokenRepositoryImpl jdbcTokenRepository(DataSource dataSource) {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        return jdbcTokenRepository;
    }

    /**
     *  AuthenticationProvider 都是放在 ProviderManager 中统一管理的，所以接下来我们就要自己提供 ProviderManager，然后注入自定义的 MyAuthenticationProvider
     * @param securityProperties
     * @param validateCodeProcessorHolder
     * @param userDetailsService
     * @return
     * @throws Exception
     */
    @Bean
    protected AuthenticationManager authenticationManager(SecurityProperties securityProperties, ValidateCodeProcessorHolder validateCodeProcessorHolder
            , UserDetailsService userDetailsService) throws Exception {
        // 让 MyAuthenticationProvider 代替 DaoAuthenticationProvider
        ProviderManager manager = new ProviderManager(Arrays.asList(myAuthenticationProvider(securityProperties, validateCodeProcessorHolder, userDetailsService)));
        return manager;
    }

    @Bean
    public MyAuthenticationProvider myAuthenticationProvider(SecurityProperties securityProperties, ValidateCodeProcessorHolder validateCodeProcessorHolder
            , UserDetailsService userDetailsService) {
        MyAuthenticationProvider myAuthenticationProvider = new MyAuthenticationProvider(securityProperties, validateCodeProcessorHolder);
        myAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        myAuthenticationProvider.setUserDetailsService(userDetailsService);
        return myAuthenticationProvider;
    }

    /**
     * 该 Bean可以将 session 创建以及销毁的事件及时感知到，并且调用 Spring 中的事件机制将相关的创建和销毁事件发布出去，进而被 Spring Security 感知到
     *
     * @return
     */
    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }

    /**
     * 用来维护会话信息的
     *
     * @return
     */
    @Bean
    public SessionRegistryImpl sessionRegistry() {
        return new SessionRegistryImpl();
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return (request, response, exception) -> {
            var isFrontBackendSeparation = "true".equals(SpringUtil.environment(PropertiesConstant.IS_FRONT_BACKEND_SEPARATION));

            if (isFrontBackendSeparation) {
                response.setContentType("application/json;charset=utf-8");
                PrintWriter out = response.getWriter();
                Response respBean = switch (exception.getClass().getSimpleName()) {
                    case "LockedException" -> Response.fail("账户被锁定，请联系管理员!");
                    case "CredentialsExpiredException" -> Response.fail("密码过期，请联系管理员!");
                    case "AccountExpiredException" -> Response.fail("账户过期，请联系管理员!");
                    case "DisabledException" -> Response.fail("账户被禁用，请联系管理员!");
                    case "BadCredentialsException" -> Response.fail("用户名或者密码输入错误，请重新输入!");
                    default -> Response.fail(exception.getMessage());
                };
                out.write(new ObjectMapper().writeValueAsString(respBean));
                out.write(exception.getMessage());
                out.flush();
                out.close();
            } else {
                response.sendRedirect("/login_error.html");
            }
        };
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return (req, resp, authentication) -> {
            var isFrontBackendSeparation = "true".equals(SpringUtil.environment(PropertiesConstant.IS_FRONT_BACKEND_SEPARATION));
            if (isFrontBackendSeparation) {
                Object principal = authentication.getPrincipal();
                resp.setContentType("application/json;charset=utf-8");
                PrintWriter out = resp.getWriter();
                out.write(new ObjectMapper().writeValueAsString(principal));
                out.flush();
                out.close();
            } else {
                resp.sendRedirect("/index.html");
            }
        };
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 角色继承
     * 在配置时，需要给角色手动加上 ROLE_ 前缀。上面的配置表示 ROLE_admin 自动具备 ROLE_user 的权限
     * @return
     */
    @Bean
    public RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl hierarchy = new RoleHierarchyImpl();
        hierarchy.setHierarchy("ROLE_admin > ROLE_user");
        return hierarchy;
    }

    /**
     * 提供一个 SpringSessionBackedSessionRegistry 的实例，并且将其配置到 sessionManagement 中去即可。
     * 以后，session 并发数据的维护将由 SpringSessionBackedSessionRegistry 来完成，而不是 SessionRegistryImpl
     * @param sessionRepository
     * @return
     */
    @Bean
    public SpringSessionBackedSessionRegistry redisSessionRegistry(FindByIndexNameSessionRepository sessionRepository){
        return new SpringSessionBackedSessionRegistry(sessionRepository);
    }
}
