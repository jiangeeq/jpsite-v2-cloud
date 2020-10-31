package com.mty.jls.code.sms;

/**
 * 验证码发送器
 * @author 掘金-蒋老湿（公众号：十分钟学编程）
 */
public interface SmsCodeSender {

    /**
     * 发送短信验证码的方法定义
     * @param mobile 手机号码
     * @param code 验证码
     */
    void send(String mobile, String code);

}
