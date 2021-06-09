package com.mty.jls.config;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;

import javax.servlet.MultipartConfigElement;

/**
 * @author jiangpeng
 * @date 2020/10/2814:36
 */
@Configuration
public class TomcatConfig {
    @Value("${spring.server.MaxFileSize}")
    private Long maxFileSize;
    @Value("${spring.server.MaxRequestSize}")
    private Long maxRequestSize;

    /**
     * spring boot项目，是内置tomcat的，通过代码的形式修改tomcat的文件限制大小
     * tomcat下的文件上传限制
     *
     * @return MultipartConfigElement
     */
    @Bean
    public MultipartConfigElement multipartConfigElement() {
        final MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize(DataSize.ofMegabytes(maxFileSize));
        factory.setMaxRequestSize(DataSize.ofMegabytes(maxRequestSize));
        return factory.createMultipartConfig();
    }

    @Bean
    TomcatServletWebServerFactory tomcatServletWebServerFactory() {
        TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory(){
            @Override
            protected void postProcessContext(Context context) {
                SecurityConstraint constraint = new SecurityConstraint();
                constraint.setUserConstraint("CONFIDENTIAL");
                SecurityCollection collection = new SecurityCollection();
                collection.addPattern("/*");
                constraint.addCollection(collection);
                context.addConstraint(constraint);
            }
        };
        // 项目已经同时支持 HTTPS 和 HTTP 了
        factory.addAdditionalTomcatConnectors(createTomcatHttpToHttpsConnector());
        factory.addAdditionalTomcatConnectors(createTomcatHttpConnector());
        return factory;
    }
    private Connector createTomcatHttpConnector() {
        Connector connector = new
                Connector("org.apache.coyote.http11.Http11NioProtocol");
        connector.setScheme("http");
        connector.setPort(8080);
        return connector;
    }

    private Connector createTomcatHttpToHttpsConnector() {
        Connector connector = new
                Connector("org.apache.coyote.http11.Http11NioProtocol");
        connector.setScheme("http");
        // 配置了 Http 的请求端口为 8081
        connector.setPort(8081);
        // Secure属性是说如果一个cookie被设置了Secure=true，那么这个cookie只能用https协议发送给服务器，用http协议是不发送的。
        // 为了同时支持 HTTPS 和 HTTP , 设置为false
        connector.setSecure(false);
        // 所有来自 8081 的请求，将被自动重定向到 8080 这个 https 的端口上。
        connector.setRedirectPort(8080);
        return connector;
    }
}
