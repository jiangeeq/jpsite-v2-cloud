package com.mty.jls.rbac.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@ApiModel("用户表")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("sys_user")
public class SysUser extends Model<SysUser> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键ID")
    @TableId(value = "user_id", type = IdType.AUTO)
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
    @TableField(exist = false)
    private List<SysRole> roleList;

    @ApiModelProperty("非数据库字段-部门名称")
    @TableField(exist = false)
    private String deptName;

    @ApiModelProperty("非数据库字段-岗位名称")
    @TableField(exist = false)
    private String jobName;

    @ApiModelProperty("")
    @TableField(exist = false)
    private String key;
}
