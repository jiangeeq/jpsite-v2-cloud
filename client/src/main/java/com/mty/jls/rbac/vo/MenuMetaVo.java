package com.mty.jls.rbac.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@ApiModel("MenuMetaVo")
@Data
@AllArgsConstructor
public class MenuMetaVo implements Serializable {
    @ApiModelProperty("菜单名称")
    private String title;

    @ApiModelProperty("图标")
    private String icon;
}
