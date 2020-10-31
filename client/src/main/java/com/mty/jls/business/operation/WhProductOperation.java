package com.mty.jls.business.operation;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.mty.jls.business.controller.reqParam.QueryProductReq;
import com.mty.jls.business.entity.WhProduct;
import com.mty.jls.business.enums.ProductStateEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author jiangpeng
 * @date 2020/10/2211:06
 */
@Slf4j
@Component
public class WhProductOperation {
    public LambdaQueryWrapper queryFieldWrapper(QueryProductReq productReq) {
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
