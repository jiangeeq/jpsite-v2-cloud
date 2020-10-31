package com.mty.jls.contract.annotations;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2SsoCustomConfiguration;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerTokenServicesConfiguration;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.security.oauth2.config.annotation.web.configuration.OAuth2ClientConfiguration;
import java.util.Set;

/**
 * 根据条件加载配置
 * @author jiangpeng
 * @date 2020/10/2017:01
 */
@Slf4j
public class Oauth2SsoSelector implements ImportSelector, EnvironmentAware, BeanFactoryAware, BeanClassLoaderAware, ResourceLoaderAware {
    private BeanFactory beanFactory;
    private ClassLoader classLoader;
    private Environment environment;
    private ResourceLoader resourceLoader;

    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        final Set<String> annotationTypes = importingClassMetadata.getAnnotationTypes();

            var isEnableOauth2Sso = "true".equals(this.environment.getProperty("is.enable.oauth2.sso"));

            if (isEnableOauth2Sso) {
                return new String[]{ OAuth2ClientConfiguration.class.getName(),
//                        OAuth2SsoDefaultConfiguration.class.getName(),
                        OAuth2SsoCustomConfiguration.class.getName(),
                        ResourceServerTokenServicesConfiguration.class.getName(),};

            }

        return new String[]{};

    }

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
}
