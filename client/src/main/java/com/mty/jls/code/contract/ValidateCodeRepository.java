package com.mty.jls.code.contract;

import com.mty.jls.code.contract.ValidateCode;
import com.mty.jls.contract.enums.ValidateCodeEnum;
import org.springframework.web.context.request.ServletWebRequest;

/**
 * 校验码存取器接口
 *
 * @author 掘金-蒋老湿（公众号：十分钟学编程）
 */
public interface ValidateCodeRepository {

    /**
     * 保存验证码
     *
     * @param request
     * @param code     验证码
     * @param codeType 验证码类型
     */
    void save(ServletWebRequest request, ValidateCode code, ValidateCodeEnum codeType);

    /**
     * 获取验证码
     *
     * @param request
     * @param codeType 验证码类型
     * @return 验证码
     */
    ValidateCode get(ServletWebRequest request, ValidateCodeEnum codeType);

    /**
     * 删除验证码
     *
     * @param request
     * @param codeType 验证码类型
     */
    void remove(ServletWebRequest request, ValidateCodeEnum codeType);

}
