package com.mty.jls.servlet;

import com.mty.jls.servlet.flter.ExceptionFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author jiangpeng
 * @date 2020/10/1413:58
 */
@Configuration
public class ServletBeanConfig {
    @Bean
    public FilterRegistrationBean<ExceptionFilter> exceptionFilterRegistration() {
        FilterRegistrationBean<ExceptionFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new ExceptionFilter());
        registration.setName("exceptionFilter");
        /* 这个序号要很小，保证 exceptionFilter 是所有过滤器链的入口 */
        registration.setOrder(Integer.MIN_VALUE);
        return registration;
    }
}
