package com.mty.jls.business.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 账号充值消费记录
 * </p>
 *
 * @author 掘金-蒋老湿（公众号：十分钟学编程）
 * @since 2020-10-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("wh_account_records")
public class WhAccountRecords extends Model<WhAccountRecords> {

    private static final long serialVersionUID = 1L;

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

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
