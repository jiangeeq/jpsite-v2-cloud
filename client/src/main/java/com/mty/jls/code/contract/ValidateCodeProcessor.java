package com.mty.jls.code.contract;

import com.mty.jls.contract.exception.ValidateCodeException;
import org.springframework.web.context.request.ServletWebRequest;

/**
 * 验证码处理器接口
 *
 * @author 掘金-蒋老湿（公众号：十分钟学编程）
 */
public interface ValidateCodeProcessor {

    /**
     * 创建校验码
     *
     * @param request
     * @throws Exception
     */
    void create(ServletWebRequest request) throws Exception;

    /**
     * 校验验证码
     *
     * @param servletWebRequest
     * @throws Exception
     */
    void validate(ServletWebRequest servletWebRequest) throws ValidateCodeException;
}
