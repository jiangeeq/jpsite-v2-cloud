package com.mty.jls.business.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author jiangpeng
 * @date 2020/10/2211:23
 */
@Data
@ApiModel("订单请求实体DTO")
public class WhOrderDTO {
    @ApiModelProperty(value = "商品编码", required = true)
    @NotBlank
    private String productCode;

    @ApiModelProperty(value = "商品名称")
    private String productName;

    @ApiModelProperty("销售数量")
    @DecimalMin("0")
    private Integer saleNum;

    @ApiModelProperty("销售总价")
    @DecimalMin("0")
    private BigDecimal totalSalePrice;

    @ApiModelProperty("销售单价")
    private BigDecimal unitPrice;

    @ApiModelProperty("起始创建时间")
    private LocalDateTime minCreateTime;

    @ApiModelProperty("结束创建时间")
    private LocalDateTime maxCreateTime;

    @ApiModelProperty("最小订单价格")
    private BigDecimal minSalePrice;

    @ApiModelProperty("最大订单价格")
    private BigDecimal maxSalePrice;
}
