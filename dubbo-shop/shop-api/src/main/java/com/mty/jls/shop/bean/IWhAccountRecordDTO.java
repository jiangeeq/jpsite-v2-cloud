package com.mty.jls.shop.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author jiangpeng
 * @date 2020/10/2214:18
 */
@Data
@ApiModel("账号充值消费记录")
public class IWhAccountRecordDTO {
    @ApiModelProperty("最小金额")
    private BigDecimal minAmount;

    @ApiModelProperty("最大金额")
    private BigDecimal maxAmount;

    @ApiModelProperty("起始创建时间")
    private LocalDateTime minCreateTime;

    @ApiModelProperty("结束创建时间")
    private LocalDateTime maxCreateTime;

    @ApiModelProperty("记录类型")
    private Integer type;
}
