package com.mty.jls.contract.model;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Objects;

/**
 * 请求响应类，强制要求契约接口的返回值必须是该类或其子类
 *
 * @param <T> payload泛型
 */
public class Response<T> implements Serializable {

    /**
     * 请求成功
     */
    public static final Integer OK = 200;

    /**
     * 已知晓请求，但未成功处理
     */
    public static final Integer ACCEPT = 202;

    /**
     * 请求失败（客户端因素）
     */
    public static final Integer BAD_REQUEST = 400;

    /**
     * 未授权
     */
    public static final Integer UNAUTHORIZED = 401;

    /**
     * 请求失败（服务端因素）
     */
    public static final Integer FORBIDDEN = 403;


    @ApiModelProperty("状态码")
    private Integer code;

    @ApiModelProperty("响应消息")
    private String message;

    @ApiModelProperty("消息负载")
    private T payload;

    /**
     * 使用默认的参数构造
     */
    public Response() {
        this(OK);
    }

    /**
     * 使用给定的参数构造
     *
     * @param code 状态码
     */
    public Response(Integer code) {
        this(code, null);
    }

    /**
     * 使用给定的参数构造
     *
     * @param code    状态码
     * @param message 响应消息
     */
    public Response(Integer code, String message) {
        this(code, message, null);
    }

    /**
     * 使用给定参数构造
     *
     * @param code    状态码
     * @param message 响应消息
     * @param payload 响应负载
     */
    public Response(Integer code, String message, T payload) {
        this.code = code;
        this.message = message;
        this.payload = payload;
    }

    /**
     * 使用给定参数构造
     *
     * @param payload 响应负载
     */
    public Response(T payload) {
        this(OK, null, payload);
    }

    /**
     * 使用默认参数构造一个响应
     *
     * @return 响应
     */
    public static <T> Response<T> succeed() {
        return new Response<>();
    }

    /**
     * 使用给定参数构造一个成功响应
     *
     * @param payload 响应负载
     * @return 成功响应
     */
    public static <T> Response<T> succeed(T payload) {
        return new Response<>(payload);
    }

    /**
     * 使用给定参数构造一个失败响应
     *
     * @param code 错误码
     * @return 失败响应
     */
    public static <T> Response<T> fail(Integer code) {
        return new Response<>(code);
    }

    public static <T> Response<T> fail(String message) {
        return new Response<>(BAD_REQUEST, message);
    }

    /**
     * 使用给定参数构造一个失败响应
     *
     * @param code    错误码
     * @param message 错误消息
     * @return 失败响应
     */
    public static <T> Response<T> fail(Integer code, String message) {
        return new Response<>(code, message);
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getPayload() {
        return payload;
    }

    public void setPayload(T payload) {
        this.payload = payload;
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, message, payload);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final Response other = (Response) obj;
        return Objects.equals(this.code, other.code)
                && Objects.equals(this.message, other.message)
                && Objects.equals(this.payload, other.payload);
    }

    @Override
    public String toString() {
        return "Response{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", payload=" + payload +
                '}';
    }
}
