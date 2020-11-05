package com.mty.jls.shop.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dove.jls.common.bean.PageRequest;
import com.dove.jls.common.bean.PageResponse;
import com.dove.jls.common.utils.BeanPlusUtil;
import com.mty.jls.shop.api.IWhProductService;
import com.mty.jls.shop.bean.IWhProduct;
import com.mty.jls.shop.bean.QueryProductReq;
import com.mty.jls.shop.dao.WhProductDao;
import com.mty.jls.shop.entity.WhProduct;
import com.mty.jls.shop.enums.ProductStateEnum;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 商品信息表 服务实现类
 * </p>
 *
 * @author 掘金-蒋老湿（公众号：十分钟学编程）
 * @since 2020-10-11
 */
@Service(cluster = "failsafe",
        loadbalance = "roundrobin",
        version = "1.0.0"
)
public class WhProductServiceImpl extends ServiceImpl<WhProductDao, WhProduct> implements IWhProductService {
    @Autowired
    private WhProductDao productDao;

    @Override
    public IWhProduct getByCode(String code) {
        final WhProduct product = productDao.getByCode(code);

        return BeanPlusUtil.copySingleProperties(product, IWhProduct::new);
    }

    @Override
    public Boolean save(IWhProduct product) {
        return baseMapper.insert(BeanPlusUtil.copySingleProperties(product, WhProduct::new)) > 0;
    }

    @Override
    public Boolean updateByCode(IWhProduct product, String code) {
        LambdaQueryWrapper<WhProduct> queryWrapper = Wrappers.<WhProduct>lambdaQuery().eq(WhProduct::getCode, code);
        final WhProduct whProduct = BeanPlusUtil.copySingleProperties(product, WhProduct::new);
        return baseMapper.update(whProduct, queryWrapper) > 0;
    }

    @Override
    public PageResponse page(PageRequest pageRequest, QueryProductReq req) {
        final Page<WhProduct> page = new Page<>(pageRequest.getPageNumber(), pageRequest.getPageSize());
        LambdaQueryWrapper<WhProduct> queryWrapper = queryFieldWrapper(req);

        final Page<WhProduct> productPage = baseMapper.selectPage(page, queryWrapper);
        final PageResponse<Collection> pageResponse = PageResponse.ok();
        pageResponse.setPage(productPage.getCurrent()).setPageSize(productPage.getSize()).setTotal(productPage.getTotal())
                .setRecords(BeanPlusUtil.copyListProperties(productPage.getRecords(), IWhProduct::new));
        return pageResponse;
    }

    @Override
    public List<IWhProduct> list(QueryProductReq req) {
        LambdaQueryWrapper<WhProduct> queryWrapper = queryFieldWrapper(req);
        final List<WhProduct> whProductList = baseMapper.selectList(queryWrapper);
        return BeanPlusUtil.copyListProperties(whProductList, IWhProduct::new);
    }

    @Override
    public Boolean removeByCode(String code) {
        LambdaQueryWrapper<WhProduct> queryWrapper = Wrappers.<WhProduct>lambdaQuery().eq(WhProduct::getCode, code);

        return baseMapper.delete(queryWrapper) > 0;
    }

    private LambdaQueryWrapper<WhProduct> queryFieldWrapper(QueryProductReq productReq) {
        LambdaQueryWrapper<WhProduct> queryWrapper = Wrappers.<WhProduct>lambdaQuery()
                .select(WhProduct::getId, WhProduct::getCode, WhProduct::getName, WhProduct::getSalePrice, WhProduct::getCategory,
                        WhProduct::getDescription, WhProduct::getDistributor, WhProduct::getState, WhProduct::getMinnumOrder,
                        WhProduct::getMaxnumOrder, WhProduct::getOrderMultiple, WhProduct::getCategoryCode)
                .eq(WhProduct::getCode, productReq.getCode())
                .or()
                .eq(WhProduct::getName, productReq.getName())
                .or()
                .eq(WhProduct::getCategory, productReq.getCategory())
                .or()
                .eq(WhProduct::getState, ProductStateEnum.SALED.getCode());

        return queryWrapper;
    }
}
