package com.mty.jls.shop.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 商品信息表
 * </p>
 *
 * @author 掘金-蒋老湿（公众号：十分钟学编程）
 * @since 2020-10-11
 */
@Data
  @EqualsAndHashCode(callSuper = false)
    @Accessors(chain = true)
  @TableName("wh_product")
public class WhProduct extends Model<WhProduct> {

    private static final long serialVersionUID = 1L;

      /**
     * id
     */
        @TableId(value = "id", type = IdType.AUTO)
      private Integer id;

      /**
     * 商品编码
     */
      private String code;

      /**
     * 商品名称
     */
      private String name;

      /**
     * 销售价
     */
      private BigDecimal salePrice;

      /**
     * 原价
     */
      private BigDecimal originalPrice;

      /**
     * 分类
     */
      private String category;

      /**
     * 详细描述
     */
      private String description;

      /**
     * 批发商
     */
      private String distributor;

      /**
     * 原始地址
     */
      private String originalUrl;

      /**
     * 状态
     */
      private Integer state;

      /**
     * 最小下单数量范围
     */
      private String minnumOrder;

      /**
     * 最大下单数量范围
     */
      private String maxnumOrder;

      /**
     * 下单倍数，如果为空或为0则没有下单倍数限制
     */
      private String orderMultiple;

    private String categoryCode;

    private LocalDateTime createTime;

    private Integer createor;

    private LocalDateTime editTime;

    private Integer editor;

    private String tenantId;

    @Override
    protected Serializable pkVal() {
          return this.id;
      }

}
