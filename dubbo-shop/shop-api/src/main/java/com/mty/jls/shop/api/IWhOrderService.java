package com.mty.jls.shop.api;

import com.dove.jls.common.bean.PageRequest;
import com.dove.jls.common.bean.PageResponse;
import com.mty.jls.shop.bean.IWhOrder;
import com.mty.jls.shop.bean.IWhOrderDTO;

import java.util.List;

/**
 * <p>
 * 销售订单表 服务类
 * </p>
 *
 * @author 掘金-蒋老湿（公众号：十分钟学编程）
 * @since 2020-10-22
 */
public interface IWhOrderService  {

    Boolean save(IWhOrder order);

    IWhOrder getById(Integer id);

    Boolean updateById(IWhOrder order);

    PageResponse<List<IWhOrder>> page(PageRequest pageRequest, IWhOrderDTO orderDTO);
}
