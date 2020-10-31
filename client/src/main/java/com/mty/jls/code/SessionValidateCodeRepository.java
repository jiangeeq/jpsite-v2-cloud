package com.mty.jls.code;


import com.mty.jls.code.contract.ValidateCode;
import com.mty.jls.code.contract.ValidateCodeRepository;
import com.mty.jls.contract.enums.ValidateCodeEnum;
import com.mty.jls.dovecommon.utils.HttpUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;

/**
 * 基于session的验证码存取器
 *
 * @author 掘金-蒋老湿（公众号：十分钟学编程）
 */
@Component
public class SessionValidateCodeRepository implements ValidateCodeRepository {
    /**
     * 验证码放入session时的前缀
     */
    private static final String SESSION_KEY_PREFIX = "SESSION_KEY_FOR_CODE_:";

    /**
     * 构建验证码放入session时的key
     *
     * @param request
     * @return
     */
    private String getSessionKey(ServletWebRequest request, ValidateCodeEnum validateCodeType) {
        return SESSION_KEY_PREFIX + request.getSessionId() + ":" + validateCodeType.toString().toUpperCase();
    }

    @Override
    public void save(ServletWebRequest request, ValidateCode validateCode, ValidateCodeEnum validateCodeType) {
        /*
         * 只取code和过期时间, 存到session, 因为存入redis的内容需要序列化
         */
        ValidateCode code = new ValidateCode(validateCode.getCode(), validateCode.getExpireTime());
        HttpUtil.setAttribute(request, getSessionKey(request, validateCodeType), code);
    }

    /**
     * 从session取出ValidateCode
     *
     * @param request
     * @param validateCodeType 校验码类型
     * @return
     */
    @Override
    public ValidateCode get(ServletWebRequest request, ValidateCodeEnum validateCodeType) {
        return (ValidateCode) HttpUtil.getAttribute(request, getSessionKey(request, validateCodeType));
    }

    /**
     * 从session删除对应的ValidateCode
     *
     * @param request
     * @param validateCodeType 校验码类型
     */
    @Override
    public void remove(ServletWebRequest request, ValidateCodeEnum validateCodeType) {
        HttpUtil.removeAttribute(request, getSessionKey(request, validateCodeType));
    }
}
