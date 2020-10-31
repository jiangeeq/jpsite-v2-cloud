package com.mty.jls.code.sms;


import com.mty.jls.code.contract.ValidateCode;
import com.mty.jls.code.contract.ValidateCodeGenerator;
import com.mty.jls.properties.SecurityProperties;
import lombok.AllArgsConstructor;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;


/**
 * 短信验证码生成器
 *
 * @author 掘金-蒋老湿（公众号：十分钟学编程）
 */
@AllArgsConstructor
public class SmsValidateCodeGenerator implements ValidateCodeGenerator {

    private SecurityProperties securityProperties;

    /**
     * 创建验证码
     *
     * @param request
     * @return
     */
    @Override
    public ValidateCode generate(ServletWebRequest request) {
        String code = RandomStringUtils.randomNumeric(securityProperties.getCode().getSms().getLength());
        return new ValidateCode(code, securityProperties.getCode().getSms().getExpireIn());
    }
}
