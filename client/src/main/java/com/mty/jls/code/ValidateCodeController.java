package com.mty.jls.code;

import com.mty.jls.code.contract.ValidateCodeProcessor;
import com.dove.jls.common.utils.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 关于校验码的url处理
 *
 * @author 掘金-蒋老湿（公众号：十分钟学编程）
 */
@RestController
public class ValidateCodeController {
    private ValidateCodeProcessorHolder validateCodeProcessorHolder;

    @Autowired
    public ValidateCodeController(ValidateCodeProcessorHolder validateCodeProcessorHolder) {
        this.validateCodeProcessorHolder = validateCodeProcessorHolder;
    }

    /**
     * 创建验证码，根据验证码类型不同，调用不同的 {@link ValidateCodeProcessor}接口实现
     *
     * @param request
     * @param response
     * @param type 验证码类型
     * @throws Exception
     */
    @PostMapping("/code/{type}")
    public String createCode(HttpServletRequest request, HttpServletResponse response, @PathVariable String type)
            throws Exception {
        validateCodeProcessorHolder.findValidateCodeProcessor(type).create(new ServletWebRequest(request, response));
        return JsonUtil.encode(new ResponseEntity<>("验证码发送成功", HttpStatus.OK));
    }

    @GetMapping("code/{type}")
    public String imageCode(HttpServletRequest request, HttpServletResponse response, @PathVariable String type)
            throws Exception {
        validateCodeProcessorHolder.findValidateCodeProcessor(type).create(new ServletWebRequest(request, response));
        return JsonUtil.encode(new ResponseEntity<>("验证码发送成功", HttpStatus.OK));
    }

}
