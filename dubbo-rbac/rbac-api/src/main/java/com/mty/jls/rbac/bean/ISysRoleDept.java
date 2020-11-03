package com.mty.jls.rbac.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;


@ApiModel("角色与部门对应关系")
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ISysRoleDept implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("部门主键ID")
    private Integer id;

    @ApiModelProperty("角色ID")
    private Integer roleId;

    @ApiModelProperty("部门ID")
    private Integer deptId;
}
