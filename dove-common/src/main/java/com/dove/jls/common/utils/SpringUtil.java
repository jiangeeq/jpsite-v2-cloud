package com.dove.jls.common.utils;

import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

/**
 * @author jiangpeng
 * @date 2020/10/1317:05
 */
public class SpringUtil {
    private static ApplicationContext context;

    public static void setApplicationContext(ApplicationContext applicationContext) {
        context = applicationContext;
    }

    public static ApplicationContext applicationContext() {
        return context;
    }

    public static <T> T getBean(Class<T> requiredType) {
        return context.getBean(requiredType);
    }

    public static Environment environment() {
        return context.getEnvironment();
    }

    public static String environment(String propertyName){
        return environment().getProperty(propertyName);
    }
}
