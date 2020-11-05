package com.mty.jls.config;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;
import java.util.List;

/**
 * @author jiangpeng
 * @date 2020/10/1014:09
 */
@Configuration
@EnableSwagger2
@EnableKnife4j
@Import(BeanValidatorPluginsConfiguration.class)
public class SwaggerConfig {
    @Bean(value = "defaultApi2")
    public Docket defaultApi2() {
        return new Docket(DocumentationType.SWAGGER_2)
                //分组名称
                .groupName("业务服务")
                .select()
                //这里指定Controller扫描包路径
                .apis(RequestHandlerSelectors.basePackage("com.mty.jls.controller.business"))
                .paths(PathSelectors.any())
                .build()
                .securityContexts((Collections.singletonList(securityContext())))
                .securitySchemes(securitySchemes())
                .apiInfo(new ApiInfoBuilder()
                        .title("业务服务 APIs")
                        .description("基于Spring boot 的快速开发平台，一个简单且技术含量不低的服务端应用。")
                        .termsOfServiceUrl("http://localhost:80/")
                        .contact(new Contact("蒋老湿", "https://github.com/jiangeeq/jpsite-v2", "773899172@qq.com"))
                        .version("1.0")
                        .build());
    }


    @Bean(value = "defaultApi1")
    public Docket defaultApi1() {
        return new Docket(DocumentationType.SWAGGER_2)
                //分组名称
                .groupName("RBAC权限服务")
                .select()
                //这里指定Controller扫描包路径
                .apis(RequestHandlerSelectors.basePackage("com.mty.jls.controller.rbac"))
                .paths(PathSelectors.any())
                .build()
                .securityContexts((Collections.singletonList(securityContext())))
                .securitySchemes(securitySchemes())
                .apiInfo(new ApiInfoBuilder()
                        .title("RBAC权限服务 APIs")
                        .description("基于Spring boot 的快速开发平台，一个简单且技术含量不低的服务端应用。")
                        .termsOfServiceUrl("http://localhost:80/")
                        .contact(new Contact("蒋老湿", "https://github.com/jiangeeq/jpsite-v2", "773899172@qq.com"))
                        .version("1.0")
                        .build());
    }

    @Bean(value = "defaultApi3")
    public Docket defaultApi3() {
        return new Docket(DocumentationType.SWAGGER_2)
                //分组名称
                .groupName("科大讯飞服务")
                .select()
                //这里指定Controller扫描包路径
                .apis(RequestHandlerSelectors.basePackage("com.mty.jls.iflytek"))
                .paths(PathSelectors.any())
                .build()
                .securityContexts((Collections.singletonList(securityContext())))
                .securitySchemes(securitySchemes())
                .apiInfo(new ApiInfoBuilder()
                        .title("科大讯飞服务 APIs")
                        .description("科大讯飞服务，提供各种人工智能技术")
                        .termsOfServiceUrl("http://localhost:80/")
                        .contact(new Contact("蒋老湿", "https://github.com/jiangeeq/jpsite-v2", "773899172@qq.com"))
                        .version("1.0")
                        .build());
    }

    /**
     * 认证方式使用密码模式
     */
    private List<SecurityScheme> securitySchemes() {
        var key1 = new ApiKey("BearerToken", "Authorization", "header");
//        var key2 = new ApiKey("BearerToken1", "Authorization-x", "header");
        return List.of(key1);
    }

    /**
     * 设置 swagger2 认证的安全上下文
     */
    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(Collections.singletonList(new SecurityReference("OAuth2", scopes())))
                .forPaths(PathSelectors.any())
                .build();
    }

    /**
     * 允许认证的scope
     */
    private AuthorizationScope[] scopes() {
        return new AuthorizationScope[]{
                new AuthorizationScope("all", "all scope")
        };
    }
}
