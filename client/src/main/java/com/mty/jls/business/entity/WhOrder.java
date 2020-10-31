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
 * 销售订单表
 * </p>
 *
 * @author 掘金-蒋老湿（公众号：十分钟学编程）
 * @since 2020-10-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("wh_order")
public class WhOrder extends Model<WhOrder> {

    private static final long serialVersionUID = 1L;

      private Integer id;

      private String no;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime editTime;

    /**
     * 修改人
     */
    private Integer editor;

    /**
     * 创建人
     */
    private Integer creator;

    /**
     * 商品编码
     */
    private Integer productCode;

    /**
     * 商品名称
     */
    private String productName;

    /**
     * 销售数量
     */
    private String saleNum;

    /**
     * 采购成本
     */
    private BigDecimal totalPurchasePrice;

    /**
     * 销售总价
     */
    private BigDecimal totalSalePrice;

    /**
     * 状态
     */
    private Integer state;

    /**
     * 批发商
     */
    private String distributor;

    /**
     * 销售单价
     */
    private BigDecimal unitPrice;

    private Boolean delFlag;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
