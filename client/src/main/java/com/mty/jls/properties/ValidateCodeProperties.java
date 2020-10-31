package com.mty.jls.properties;

import lombok.Data;

/**
 * 验证码配置属性
 * @author 掘金-蒋老湿（公众号：十分钟学编程）
 */
@Data
public class ValidateCodeProperties {
    /**
     * 图片验证码配置
     */
    private ImageCodeProperties image = new ImageCodeProperties();
    /**
     * 短信验证码配置
     */
    private SmsCodeProperties sms = new SmsCodeProperties();
}
