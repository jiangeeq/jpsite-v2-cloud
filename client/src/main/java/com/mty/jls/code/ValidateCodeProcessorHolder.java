package com.mty.jls.code;

import com.mty.jls.code.contract.ValidateCodeProcessor;
import com.mty.jls.contract.enums.ValidateCodeEnum;
import com.mty.jls.contract.exception.ValidateCodeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;

/**
 * 验证码校验处理的管理器
 *
 * @author 掘金-蒋老湿（公众号：十分钟学编程）
 */
@Component
public class ValidateCodeProcessorHolder {
    /**
     * Spring启动时，会查找容器中所有的 {@link ValidateCodeProcessor} 接口的实现，并把Bean的名字作为key，放到map中
     */
    private Map<String, ValidateCodeProcessor> validateCodeProcessors;

    @Autowired
    public ValidateCodeProcessorHolder(Map<String, ValidateCodeProcessor> validateCodeProcessors) {
        this.validateCodeProcessors = validateCodeProcessors;
    }

    /**
     * 根据验证码类型获取处理器
     *
     * @param type 验证码类型
     * @return 验证码处理器
     */
    public ValidateCodeProcessor findValidateCodeProcessor(ValidateCodeEnum type) {
        return findValidateCodeProcessor(type.toString().toLowerCase());
    }

    /**
     * 根据验证码类型找到具体验证码处理器
     *
     * @param type 校验码类型
     * @return 校验码处理器
     */
    ValidateCodeProcessor findValidateCodeProcessor(String type) {
        String beanName = type.toLowerCase() + ValidateCodeProcessor.class.getSimpleName();
        ValidateCodeProcessor processor = validateCodeProcessors.get(beanName);
        if (Objects.isNull(processor)) {
            throw new ValidateCodeException("验证码处理器" + beanName + "不存在");
        }
        return processor;
    }

}
