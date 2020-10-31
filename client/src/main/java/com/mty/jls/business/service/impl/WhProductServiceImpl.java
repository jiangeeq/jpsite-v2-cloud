package com.mty.jls.business.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.mty.jls.business.dao.WhProductDao;
import com.mty.jls.business.entity.WhProduct;
import com.mty.jls.business.service.WhProductService;
import com.mty.jls.business.vo.WhOrderVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 商品信息表 服务实现类
 * </p>
 *
 * @author 掘金-蒋老湿（公众号：十分钟学编程）
 * @since 2020-10-11
 */
@Service
public class WhProductServiceImpl extends ServiceImpl<WhProductDao, WhProduct> implements WhProductService {
    @Autowired
    private WhProductDao productDao;

    @Override
    public WhProduct getByCode(String code) {
        return productDao.getByCode(code);
    }
}
