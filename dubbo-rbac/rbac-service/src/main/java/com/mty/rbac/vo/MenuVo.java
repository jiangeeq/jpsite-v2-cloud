package com.mty.rbac.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel("菜单Vo")
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class MenuVo {
    @ApiModelProperty("菜单名称")
    private String name;

    @ApiModelProperty("前端path / 即跳转路由")
    private String path;

    @ApiModelProperty("外链地址")
    private String redirect;

    @ApiModelProperty("菜单组件")
    private String component;

    @ApiModelProperty("是否一直显示")
    private Boolean alwaysShow;

    @ApiModelProperty("元数据")
    private MenuMetaVo meta;

    @ApiModelProperty("子菜单")
    private List<MenuVo> children;
}
