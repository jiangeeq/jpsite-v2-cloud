package com.mty.jls.rbac.dto;

import com.mty.jls.rbac.domain.SysRoleMenu;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;


@ApiModel("角色DTO")
@Data
public class RoleDTO {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty("角色主键")
    private Integer roleId;

    @ApiModelProperty("角色名称")
    private String roleName;

    @ApiModelProperty("角色标识")
    private String roleCode;

    @ApiModelProperty("角色描述")
    private String roleDesc;

    @ApiModelProperty("删除标识（0-正常,1-删除）")
    private String delFlag;

    @ApiModelProperty("数据权限类型")
    private int dsType;

    @ApiModelProperty("角色菜单")
    List<SysRoleMenu> roleMenus;

    @ApiModelProperty("部门ids")
    List<Integer> roleDeptIds;



}
