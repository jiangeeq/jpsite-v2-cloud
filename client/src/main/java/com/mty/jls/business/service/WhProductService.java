package com.mty.jls.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mty.jls.business.entity.WhProduct;
import com.mty.jls.business.vo.WhOrderVo;

import java.util.List;

/**
 * <p>
 * 商品信息表 服务类
 * </p>
 *
 * @author 掘金-蒋老湿（公众号：十分钟学编程）
 * @since 2020-10-11
 */
public interface WhProductService extends IService<WhProduct> {
    WhProduct getByCode(String code);
}
