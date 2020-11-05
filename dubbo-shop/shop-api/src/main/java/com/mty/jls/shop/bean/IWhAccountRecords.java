package com.mty.jls.shop.bean;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 账号充值消费记录
 * </p>
 *
 * @author 掘金-蒋老湿（公众号：十分钟学编程）
 * @since 2020-10-22
 */
@Data
@Accessors(chain = true)
public class IWhAccountRecords implements Serializable {


      private Integer id;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 备注说明
     */
    private String comment;

    /**
     * 金额
     */
    private BigDecimal amount;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 创建人
     */
    private Integer creator;

    /**
     * 交易流水号
     */
    private String tradeNo;

    /**
     * 变动后的值
     */
    private BigDecimal newValue;

    /**
     * 变动前的值
     */
    private BigDecimal oldValue;

    /**
     * 操作类型
     */
    private Integer type;


    private Long tenantId;
}
