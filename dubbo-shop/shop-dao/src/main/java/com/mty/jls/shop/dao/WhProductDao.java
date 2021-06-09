package com.mty.jls.shop.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mty.jls.shop.entity.WhProduct;

/**
 * <p>
 * 商品信息表 Mapper 接口
 * </p>
 *
 * @author 掘金-蒋老湿（公众号：十分钟学编程）
 * @since 2020-10-11
 */
public interface WhProductDao extends BaseMapper<WhProduct> {
    /**
     * 根据商品编码获取商品信息
     *
     * @param code
     * @return
     */
    WhProduct getByCode(String code);
}
