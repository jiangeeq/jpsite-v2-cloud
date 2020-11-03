package com.mty.jls.rbac.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@ApiModel("菜单权限表")
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ISysMenu implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("菜单ID")
    private Integer menuId;

    @ApiModelProperty("菜单名称")
    private String name;

    @ApiModelProperty("菜单权限标识")
    private String perms;

    @ApiModelProperty("前端path / 即跳转路由")
    private String path;

    @ApiModelProperty("菜单组件")
    private String component;

    @ApiModelProperty("父菜单ID")
    private Integer parentId;

    @ApiModelProperty("图标")
    private String icon;

    @ApiModelProperty("是否为外链")
    private Boolean isFrame;

    @ApiModelProperty("排序")
    private Integer sort;

    @ApiModelProperty("菜单类型 （类型   0：目录   1：菜单   2：按钮）")
    private Integer type;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty("逻辑删除标记(0--正常 1--删除)")
    private String delFlag;

    @ApiModelProperty("非数据库字段-父菜单名称")
    private String parentName;

    @ApiModelProperty("非数据库字段-菜单等级")
    private Integer level;

    @ApiModelProperty("非数据库字段-子菜单")
    private List<ISysMenu> children;
}
