package com.mty.jls.contract.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * 验证码异常对象
 * @author 掘金-蒋老湿（公众号：十分钟学编程）
 */
public class ValidateCodeException extends AuthenticationException {

    public ValidateCodeException(String msg) {
        super(msg);
    }
    public ValidateCodeException(String msg, Throwable t) {
        super(msg, t);
    }

}
