package com.mty.jls.code.contract;

import com.mty.jls.contract.enums.ValidateCodeEnum;
import com.mty.jls.contract.exception.ValidateCodeException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletWebRequest;

import java.util.Map;
import java.util.Objects;

/**
 * 抽象的图片验证码处理器
 * @author 掘金-蒋老湿（公众号：十分钟学编程）
 */
public abstract class AbstractValidateCodeProcessor<C extends ValidateCode> implements ValidateCodeProcessor {
    /**
     * 收集系统中所有的 {@link ValidateCodeGenerator} 接口的实现。
     */
    @Autowired
    private Map<String, ValidateCodeGenerator> validateCodeGeneratorMap;
    @Autowired(required = false)
    private ValidateCodeRepository validateCodeRepository;

    @Override
    public void create(ServletWebRequest request) throws Exception {
        C validateCode = generate(request);
        save(request, validateCode);
        send(request, validateCode);
    }

    /**
     * 保存验证码到session
     *
     * @param request
     * @param validateCode
     */
    public void save(ServletWebRequest request, C validateCode) {
        /*
         * 只取code和过期时间存到session, 因为存入redis的内容需要序列化
         */
        ValidateCode code = new ValidateCode(validateCode.getCode(), validateCode.getExpireTime());
        validateCodeRepository.save(request, code, getValidateCodeType(request));
    }

    /**
     * 获取对应validateCodeGenerator实现类生成验证码
     *
     * @param request
     * @return
     */
    private C generate(ServletWebRequest request) {
        String codeType = getProcessorType(request);
        ValidateCodeGenerator validateCodeGenerator =
                validateCodeGeneratorMap.get(codeType + ValidateCodeGenerator.class.getSimpleName());
        return (C) validateCodeGenerator.generate(request);
    }

    private String getProcessorType(ServletWebRequest request) {
        return StringUtils.substringAfter(request.getRequest().getRequestURI(), "/code/");
    }

    /**
     * 发送验证码抽象方法，由子类实现
     *
     * @param request
     * @param validateCode
     * @throws Exception
     */
    public abstract void send(ServletWebRequest request, C validateCode) throws Exception;

    /**
     * 根据请求的url获取校验码的类型
     *
     * @param request
     * @return
     */
    private ValidateCodeEnum getValidateCodeType(ServletWebRequest request) {
        /* 截取当前具体类的前缀SimpleName @Component("imageValidateCodeProcessor")， 找到对应名称 ValidateCodeProcess 抽象类的实现类*/
        String type = StringUtils.substringBefore(getClass().getSimpleName(), "ValidateCodeProcess");
        return ValidateCodeEnum.valueOf(type.toUpperCase());
    }

    @Override
    public void validate(ServletWebRequest request) throws ValidateCodeException {

        ValidateCodeEnum codeType = getValidateCodeType(request);
        /*获取session中的验证码*/
        C codeInSession = (C) validateCodeRepository.get(request, codeType);

        String codeInRequest;
        /* 获取request携带的验证码*/
//            codeInRequest = ServletRequestUtils.getStringParameter(request.getRequest(),
//                    codeType.getParamNameOnValidate());
        codeInRequest = (String) request.getAttribute(codeType.getParamNameOnValidate(), RequestAttributes.SCOPE_REQUEST);

        if (StringUtils.isBlank(codeInRequest)) {
            throw new ValidateCodeException(codeType + "验证码的值不能为空");
        }

        if (Objects.isNull(codeInSession)) {
            throw new ValidateCodeException(codeType + "验证码不存在");
        }

        if (codeInSession.isExpired()) {
            validateCodeRepository.remove(request, codeType);
            throw new ValidateCodeException(codeType + "验证码已过期");
        }

        if (!StringUtils.equals(codeInSession.getCode(), codeInRequest)) {
            throw new ValidateCodeException(codeType + "验证码不匹配");
        }
        /* 登录成功后则删除session validateCode*/
        validateCodeRepository.remove(request, codeType);
    }
}
