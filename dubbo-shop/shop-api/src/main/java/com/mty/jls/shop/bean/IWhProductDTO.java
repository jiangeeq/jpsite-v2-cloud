package com.mty.jls.shop.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author jiangpeng
 * @date 2020/10/1210:07
 */
@Data
@ApiModel("商品请求实体DTO")
public class IWhProductDTO {
    @ApiModelProperty(value = "商品编码", required = true)
    @NotBlank
    private String code;

    @ApiModelProperty(value = "商品名称", required = true)
    @NotBlank
    private String name;

    @ApiModelProperty(value = "销售价", required = true)
    @NotNull
    private BigDecimal salePrice;

    @ApiModelProperty(value = "原价", required = true)
    @NotNull
    private BigDecimal originalPrice;

    @ApiModelProperty(value = "分类", required = true)
    @NotBlank
    private String category;

    @ApiModelProperty(value = "详细描述")
    private String description;

    @ApiModelProperty(value = "批发商", required = true)
    @NotBlank
    private String distributor;

    @ApiModelProperty(value = "原始地址")
    private String originalUrl;

    @ApiModelProperty(value = "状态", required = true)
    @NotNull
    private Integer state;

    @ApiModelProperty(value = "最小下单数量范围", required = true)
    @Min(1)
    private Integer minnumOrder;

    @ApiModelProperty(value = "最大下单数量范围", required = true)
    private Integer maxnumOrder;

    @ApiModelProperty(value = "下单倍数，如果为空或为0则没有下单倍数限制")
    private Integer orderMultiple;

    @ApiModelProperty(value = "分类编码", required = true)
    private String categoryCode;
}
