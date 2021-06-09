package com.mty.jls.shop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dove.jls.common.bean.PageRequest;
import com.dove.jls.common.bean.PageResponse;
import com.dove.jls.common.utils.BeanPlusUtil;
import com.mty.jls.shop.api.IWhOrderService;
import com.mty.jls.shop.bean.IWhOrder;
import com.mty.jls.shop.bean.IWhOrderDTO;
import com.mty.jls.shop.dao.WhOrderDao;
import com.mty.jls.shop.entity.WhOrder;
import org.apache.dubbo.config.annotation.Service;

import java.util.List;

/**
 * <p>
 * 销售订单表 服务实现类
 * </p>
 *
 * @author 掘金-蒋老湿（公众号：十分钟学编程）
 * @since 2020-10-22
 */
@Service(cluster = "failsafe",
        loadbalance = "roundrobin",
        version = "1.0.0"
)
public class WhOrderServiceImpl extends ServiceImpl<WhOrderDao, WhOrder> implements IWhOrderService {

    @Override
    public Boolean save(IWhOrder order) {
        return baseMapper.insert(BeanPlusUtil.copySingleProperties(order, WhOrder::new)) > 0;
    }

    @Override
    public IWhOrder getById(Integer id) {
        final WhOrder whOrder = baseMapper.selectById(id);
        return BeanPlusUtil.copySingleProperties(whOrder, IWhOrder::new);
    }

    @Override
    public Boolean updateById(IWhOrder order) {
        return baseMapper.updateById(BeanPlusUtil.copySingleProperties(order, WhOrder::new)) > 0;
    }

    @Override
    public PageResponse<List<IWhOrder>> page(PageRequest pageRequest, IWhOrderDTO orderDTO) {
        final LambdaQueryWrapper<WhOrder> queryWrapper = Wrappers.<WhOrder>lambdaQuery().eq(WhOrder::getProductCode, orderDTO.getProductCode())
                .or()
                .eq(WhOrder::getProductName, orderDTO.getProductName())
                .or()
                .between(WhOrder::getCreateTime, orderDTO.getMinCreateTime(), orderDTO.getMaxCreateTime())
                .or()
                .between(WhOrder::getTotalSalePrice, orderDTO.getMinSalePrice(), orderDTO.getMaxSalePrice());

        final Page<WhOrder> page = new Page<>(pageRequest.getPageNumber(), pageRequest.getPageSize());

        final Page<WhOrder> whOrderPage = baseMapper.selectPage(page, queryWrapper);

        final PageResponse<List<IWhOrder>> pageResponse = PageResponse.ok();

        pageResponse.setPage(whOrderPage.getCurrent()).setPageSize(whOrderPage.getSize()).setTotal(whOrderPage.getTotal())
                .setRecords(BeanPlusUtil.copyListProperties(whOrderPage.getRecords(), IWhOrder::new));
        return pageResponse;
    }
}
