package com.mty.jls.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 基础安全属性配置类
 * @author 掘金-蒋老湿（公众号：十分钟学编程）
 */
@Component
@ConfigurationProperties(prefix = "jpsite.security")
@Data
@EnableConfigurationProperties(SecurityProperties.class)
public class SecurityProperties {
    /**
     * 验证码配置
     */
    private ValidateCodeProperties code = new ValidateCodeProperties();
}

