package com.mty.jls.code;


import com.mty.jls.code.contract.ValidateCodeGenerator;
import com.mty.jls.code.image.ImageValidateCodeGenerator;
import com.mty.jls.code.sms.DefaultSmsCodeSender;
import com.mty.jls.code.sms.SmsCodeSender;
import com.mty.jls.code.sms.SmsValidateCodeGenerator;
import com.mty.jls.properties.SecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 验证码相关的扩展配置。在这里的声明的bean，
 * 业务系统都可以通过声明同类型或同名的bean来覆盖安全这里默认的配置
 *
 * @author 掘金-蒋老湿（公众号：十分钟学编程）
 */
@Configuration
public class ValidateCodeBeanConfig {
    private final SecurityProperties securityProperties;

    @Autowired
    public ValidateCodeBeanConfig(SecurityProperties securityProperties) {
        this.securityProperties = securityProperties;
    }

    /**
     * 图片验证码图片生成器
     *
     * @return ValidateCodeGenerator
     */
    @Bean
    @ConditionalOnMissingBean(name = "imageValidateCodeGenerator")
    public ValidateCodeGenerator imageValidateCodeGenerator() {
        ImageValidateCodeGenerator codeGenerator = new ImageValidateCodeGenerator(securityProperties);
        return codeGenerator;
    }

    /**
     * 图片验证码图片生成器
     *
     * @return ValidateCodeGenerator
     */
    @Bean
    @ConditionalOnMissingBean(name = "smsValidateCodeGenerator")
    public ValidateCodeGenerator smsValidateCodeGenerator() {
        SmsValidateCodeGenerator codeGenerator = new SmsValidateCodeGenerator(securityProperties);
        return codeGenerator;
    }

    /**
     * 短信验证码发送器
     *
     * @return SmsCodeSender
     */
    @Bean
    @ConditionalOnMissingBean(SmsCodeSender.class)
    public SmsCodeSender smsCodeSender() {
        return new DefaultSmsCodeSender();
    }

}
