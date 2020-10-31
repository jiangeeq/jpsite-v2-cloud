package com.mty.jls.config;

import org.springframework.beans.factory.annotation.Value;
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
}
