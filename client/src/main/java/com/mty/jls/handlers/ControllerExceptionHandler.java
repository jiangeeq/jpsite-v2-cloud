package com.mty.jls.handlers;

import com.mty.jls.contract.exception.BusinessException;
import com.mty.jls.contract.exception.ValidateCodeException;
import com.mty.jls.contract.model.Response;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author jiangpeng
 * @date 2020/10/1410:49
 */
@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(ValidateCodeException.class)
    public Response validateCodeException(ValidateCodeException e){
        return Response.fail(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public Response exception(Exception e){
        return Response.fail(e.getMessage());
    }

    @ExceptionHandler(BusinessException.class)
    public Response businessException(BusinessException e){
        return Response.fail(e.getCode(), e.getMessage());
    }
}
