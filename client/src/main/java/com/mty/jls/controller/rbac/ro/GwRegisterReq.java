package com.mty.jls.controller.rbac.ro;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author jiangpeng
 * @date 2020/10/2217:41
 */
@Data
@ApiModel("注册请求实体")
public class GwRegisterReq {
    @ApiModelProperty("用户名")
    @NotBlank
    private String username;

    @ApiModelProperty("密码")
    @NotBlank
    private String password;

    @ApiModelProperty("手机号")
    @NotBlank
    private String phone;

    @ApiModelProperty("短信验证码")
    @NotBlank
    private String smsCode;
}
