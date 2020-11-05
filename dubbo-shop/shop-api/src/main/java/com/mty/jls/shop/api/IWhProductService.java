package com.mty.jls.shop.api;


import com.dove.jls.common.bean.PageRequest;
import com.dove.jls.common.bean.PageResponse;
import com.mty.jls.shop.bean.IWhProduct;
import com.mty.jls.shop.bean.QueryProductReq;

import java.util.List;

/**
 * <p>
 * 商品信息表 服务类
 * </p>
 *
 * @author 掘金-蒋老湿（公众号：十分钟学编程）
 * @since 2020-10-11
 */
public interface IWhProductService {
    IWhProduct getByCode(String code);

    Boolean save(IWhProduct product);

    Boolean updateByCode(IWhProduct product, String code);

    PageResponse<List<IWhProduct>> page(PageRequest pageRequest, QueryProductReq req);

    List<IWhProduct> list(QueryProductReq req);

    Boolean removeByCode(String code);
}
