package com.mty.jls.contract.exception;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @Classname BaseException
 * @Description 自定义异常
 * @Author 蒋老湿 蒋老湿
 * @Date 2019-03-29 13:21
 * @Version 1.0
 */
public class BaseException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = 1L;

    @Setter
    @Getter
    private String msg;

    @Setter
    @Getter
    private int code = 500;

    public BaseException(String msg) {
        super(msg);
        this.msg = msg;
    }

    public BaseException(String msg, Throwable e) {
        super(msg, e);
        this.msg = msg;
    }

    public BaseException(String msg, int code) {
        super(msg);
        this.msg = msg;
        this.code = code;
    }

    public BaseException(String msg, int code, Throwable e) {
        super(msg, e);
        this.msg = msg;
        this.code = code;
    }

}
