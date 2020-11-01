package com.mty.rbac.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@ApiModel("用户DTO")
@Data
@Accessors(chain = true)
public class UserDTO implements Serializable {
    @ApiModelProperty("主键ID")
    private Integer userId;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty("部门ID")
    private Integer deptId;

    @ApiModelProperty("岗位ID")
    private Integer jobId;

    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("头像")
    private String avatar;

    @ApiModelProperty("锁定状态 0-正常，1-锁定")
    private String lockFlag;

    @ApiModelProperty("删除标识 0-正常，1-删除")
    private String delFlag;

    @ApiModelProperty("角色列表")
    private List<Integer> roleList;

    @ApiModelProperty("部门名称")
    private List<Integer> deptList;

    @ApiModelProperty("新密码")
    private String newPassword;

    @ApiModelProperty("短信验证码")
    private String smsCode;


}
