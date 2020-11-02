package com.mty.jls.config.security.provider;

import com.mty.jls.code.ValidateCodeProcessorHolder;
import com.mty.jls.contract.constant.PropertiesConstant;
import com.mty.jls.contract.enums.ValidateCodeEnum;
import com.dove.jls.common.utils.SpringUtil;
import com.mty.jls.properties.SecurityProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 自定义一个 AuthenticationProvider 代替 DaoAuthenticationProvider
 */
@Slf4j
public class MyAuthenticationProvider extends DaoAuthenticationProvider {

    private final SecurityProperties securityProperties;
    private final ValidateCodeProcessorHolder validateCodeProcessorHolder;

    // 存放所有需要校验验证码的url
    private final static Map<String, ValidateCodeEnum> urlMap = new HashMap<>();
    // 验证请求url与配置的url是否匹配的工具类
    private final static AntPathMatcher pathMatcher = new AntPathMatcher();

    public MyAuthenticationProvider(SecurityProperties securityProperties, ValidateCodeProcessorHolder validateCodeProcessorHolder) {
        super();
        this.securityProperties = securityProperties;
        this.validateCodeProcessorHolder = validateCodeProcessorHolder;
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        final String isEnableLoginVerifyCode = SpringUtil.environment(PropertiesConstant.IS_ENABLE_LOGIN_VERIFY_CODE);

        if ("true".equals(isEnableLoginVerifyCode)) {
            // 动态配置url
            addUrlToMap(securityProperties.getCode().getImage().getUrl(), ValidateCodeEnum.IMAGE);
            addUrlToMap(securityProperties.getCode().getSms().getUrl(), ValidateCodeEnum.SMS);

            ValidateCodeEnum type = getValidateCodeType(request);
            if (Objects.nonNull(type)) {
                log.info(String.format("请求[%s]需要验证码,验证码类型[%s]", request.getRequestURI(), type));
                validateCodeProcessorHolder.findValidateCodeProcessor(type).validate(new ServletWebRequest(request));
            } else {
                log.info("请求[{}]不需要验证码校验，[{}]放行", request.getRequestURI(), this.getClass());
            }

        }
        // 最后通过 super 调用父类方法，也就是 DaoAuthenticationProvider 的 additionalAuthenticationChecks 方法，该方法中主要做密码的校验
        super.additionalAuthenticationChecks(userDetails, authentication);
    }

    /**
     * 将配置文件中需要校验验证码的URL根据校验的类型放入map
     *
     * @param urlString url集合
     * @param type      验证码类型
     */
    private void addUrlToMap(String urlString, ValidateCodeEnum type) {
        if (StringUtils.isNotBlank(urlString)) {
            String[] urls = StringUtils.splitByWholeSeparatorPreserveAllTokens(urlString, ",");
            for (String url : urls) {
                urlMap.put(url, type);
            }
        }
    }

    /**
     * 获取校验码的类型，如果当前请求不需要校验，则返回null
     *
     * @param request
     * @return
     */
    public ValidateCodeEnum getValidateCodeType(HttpServletRequest request) {
        // 只支持post请求匹配
        if (StringUtils.equalsIgnoreCase(request.getMethod(), "POST")) {
            for (String url : urlMap.keySet()) {
                if (pathMatcher.match(url, request.getRequestURI())) {
                    return urlMap.get(url);
                }
            }
        }
        return null;
    }
}
