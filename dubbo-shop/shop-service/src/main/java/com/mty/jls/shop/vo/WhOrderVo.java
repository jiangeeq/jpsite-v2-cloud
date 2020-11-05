package com.mty.jls.shop.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@ApiModel("订单实体vo")
public class WhOrderVo {
    @ApiModelProperty(value = "id")
    private Integer id;

    @ApiModelProperty(value = "用户ID")
    private Integer userId;

    @ApiModelProperty(value = "创建时间")
    @NotNull
    private LocalDateTime createTime;

    @ApiModelProperty(value = "修改时间")
    @NotNull
    private LocalDateTime editTime;

    @ApiModelProperty(value = "修改人")
    private String editor;

    @ApiModelProperty(value = "创建人")
    private String createor;

    @ApiModelProperty(value = "商品编码", required = true)
    @NotBlank
    private Integer productCode;

    @ApiModelProperty(value = "商品名称", required = true)
    @NotBlank
    private String productName;

    @ApiModelProperty(value = "销售数量", required = true)
    @NotBlank
    private String saleNum;

    @ApiModelProperty(value = "采购成本", required = true)
    @NotBlank
    private BigDecimal totalPurchasePrice;

    @ApiModelProperty(value = "销售总价", required = true)
    private BigDecimal totalSalePrice;

    @ApiModelProperty(value = "状态", required = true)
    @NotNull
    private Integer state;

    @ApiModelProperty(value = "批发商", required = true)
    private Integer distributor;

    @ApiModelProperty(value = "销售单价", required = true)
    @NotNull
    private BigDecimal unitPrice;

    @ApiModelProperty(value = "租户ID")
    private Integer tenantId;

}
