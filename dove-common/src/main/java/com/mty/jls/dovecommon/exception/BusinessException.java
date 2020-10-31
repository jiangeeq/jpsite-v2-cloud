package com.mty.jls.dovecommon.exception;

import org.springframework.http.HttpStatus;

/**
 * 业务异常基类，可以认为是业务信息的一种表示，而非程序错误
 */
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = -2300496708184362237L;

    private long code = HttpStatus.ACCEPTED.value();

    public BusinessException() {

    }

    public BusinessException(long code) {
        this.code = code;
    }

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(long code, String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }

    public BusinessException(long code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public BusinessException(Throwable cause) {
        super(cause);
    }

    public BusinessException(long code, Throwable cause) {
        super(cause);
        this.code = code;
    }

    public BusinessException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public BusinessException(long code, String message, Throwable cause, boolean enableSuppression, boolean
            writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.code = code;
    }

    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }
}
