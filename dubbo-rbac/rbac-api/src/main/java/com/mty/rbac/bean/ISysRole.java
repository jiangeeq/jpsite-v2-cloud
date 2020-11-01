package com.mty.rbac.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;


@ApiModel("系统角色表")
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ISysRole {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("角色主键")
    private Integer roleId;

    @ApiModelProperty("角色名称")
    private String roleName;

    @ApiModelProperty("角色标识")
    private String roleCode;

    @ApiModelProperty("角色描述")
    private String roleDesc;

    @ApiModelProperty("数据权限类型")
    private int dsType;

    @ApiModelProperty("数据权限范围")
    private String dsScope;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty("删除标识（0-正常,1-删除）")
    private String delFlag;

    @ApiModelProperty("非数据库字段-部门ids")
    private List<Integer> roleDeptIds;
}
