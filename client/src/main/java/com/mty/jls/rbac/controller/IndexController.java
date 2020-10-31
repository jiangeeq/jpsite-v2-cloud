package com.mty.jls.rbac.controller;

import com.mty.jls.code.contract.AbstractValidateCodeProcessor;
import com.mty.jls.code.contract.ValidateCode;
import com.mty.jls.code.contract.ValidateCodeGenerator;
import com.mty.jls.contract.exception.ValidateCodeException;
import com.mty.jls.contract.model.Response;
import com.mty.jls.dovecommon.utils.BeanPlusUtil;
import com.mty.jls.rbac.controller.reqModel.GwRegisterReq;
import com.mty.jls.rbac.domain.SysUser;
import com.mty.jls.rbac.dto.UserDTO;
import com.mty.jls.rbac.service.ISysUserService;
import com.mty.jls.utils.RbacUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * @Classname IndexController
 * @Description 主页模块
 * @Author 蒋老湿 蒋老湿
 * @Date 2019-05-07 12:38
 * @Version 1.0
 */
@Slf4j
@Api(value = "主页管理", tags = "主页管理")
@RestController
public class IndexController {
    @Autowired
    private ISysUserService userService;
    @Autowired
    private ValidateCodeGenerator smsValidateCodeGenerator;
    @Autowired
    private AbstractValidateCodeProcessor<ValidateCode> smsValidateCodeProcessor;


    @ApiOperation(value = "发送短信验证码")
    @PostMapping("/sendCode")
    public Response<String> sendSmsCode(@RequestParam String mobile, HttpServletRequest request) throws Exception {
        ServletWebRequest res = new ServletWebRequest(request);
        ValidateCode validateCode = smsValidateCodeGenerator.generate(res);
        smsValidateCodeProcessor.send(res, validateCode);
        smsValidateCodeProcessor.save(res, validateCode);
        return Response.succeed("发送成功");
    }

    @ApiOperation(value = "用户注册")
    @PostMapping("/register")
    public Response register(@Valid @RequestBody GwRegisterReq registerUserReq, HttpServletRequest request) throws IllegalAccessException {
        try {
            final ServletWebRequest servletWebRequest = new ServletWebRequest(request);
            servletWebRequest.setAttribute("smsCode", registerUserReq.getSmsCode(), RequestAttributes.SCOPE_REQUEST);
            smsValidateCodeProcessor.validate(servletWebRequest);
            final UserDTO userDTO = BeanPlusUtil.copySingleProperties(registerUserReq, UserDTO::new);
            return Response.succeed(userService.register(userDTO));
        } catch (ValidateCodeException e) {
            log.error("用户注册失败", e);
            return Response.fail(e.getMessage());
        }
    }

    @ApiOperation(value = "查看详情")
    @GetMapping("/info")
    public Response<SysUser> info() {
        final SysUser user = userService.findByUserInfoName(RbacUtil.getSecurityUser().getUsername());
        return Response.succeed(user);
    }
}
