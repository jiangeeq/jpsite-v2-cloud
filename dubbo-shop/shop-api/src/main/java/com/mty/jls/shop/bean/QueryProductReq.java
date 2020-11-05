package com.mty.jls.shop.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author jiangpeng
 * @date 2020/10/2216:02
 */
@Data
@ApiModel("查询商品请求参数")
public class QueryProductReq {
    @ApiModelProperty(value = "商品编码")
    private String code;

    @ApiModelProperty(value = "商品名称")
    private String name;

    @ApiModelProperty(value = "分类")
    private String category;

    @ApiModelProperty(value = "状态")
    private Integer state;
}
