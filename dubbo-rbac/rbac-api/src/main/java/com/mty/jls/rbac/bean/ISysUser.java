package com.mty.jls.rbac.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@ApiModel("用户表")
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class ISysUser {

    private static final long serialVersionUID = 1L;

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

    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("头像")
    private String avatar;

    @ApiModelProperty("余额")
    private BigDecimal balance;

    @ApiModelProperty("租户id")
    private Long tenantId;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("修改时间")
    private LocalDateTime updateTime;

    @ApiModelProperty("锁定状态 0-正常，1-锁定")
    private String lockFlag;

    @ApiModelProperty("删除标识 0-正常，1-删除")
    private String delFlag;

    @ApiModelProperty("非数据库字段-角色列表")
    private List<ISysRole> roleList;

    @ApiModelProperty("非数据库字段-部门名称")
    private String deptName;

    @ApiModelProperty("非数据库字段-岗位名称")
    private String jobName;

    @ApiModelProperty("")
    private String key;
}
