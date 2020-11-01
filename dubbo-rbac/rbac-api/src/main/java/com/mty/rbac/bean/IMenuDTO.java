package com.mty.rbac.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Classname UserDTO
 * @Description 菜单Dto
 * @Author 蒋老湿 蒋老湿
 * @Date 2019-04-23 21:26
 * @Version 1.0
 */
@ApiModel("菜单DTO")
@Data
@EqualsAndHashCode(callSuper = false)
public class IMenuDTO {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("菜单ID")
    private Integer menuId;

    @ApiModelProperty("菜单名称")
    private String name;

    @ApiModelProperty("菜单权限标识")
    private String perms;

    @ApiModelProperty("前端path / 即跳转路由")
    private String path;

    @ApiModelProperty("是否为外链")
    private Boolean isFrame;

    @ApiModelProperty("父菜单ID")
    private Integer parentId;

    @ApiModelProperty("菜单组件")
    private String component;

    @ApiModelProperty("图标")
    private String icon;

    @ApiModelProperty("排序")
    private Integer sort;

    @ApiModelProperty("菜单类型 （类型   0：目录   1：菜单   2：按钮）")
    private Integer type;

    @ApiModelProperty("逻辑删除标记(0--正常 1--删除)")
    private String delFlag;
}
