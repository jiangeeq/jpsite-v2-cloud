package com.mty.jls.code.sms;

import lombok.extern.slf4j.Slf4j;

/**
 * 默认的短信验证码发送器
 *
 * @author 掘金-蒋老湿（公众号：十分钟学编程）
 */
@Slf4j
public class DefaultSmsCodeSender implements SmsCodeSender {
    @Override
    public void send(String mobile, String code) {
        log.warn("请对接短信服务商，配置真实的短信验证码发送器(SmsCodeSender)");
        log.info("向手机[{}]发送短信验证码[{}]", mobile, code);
    }
}
