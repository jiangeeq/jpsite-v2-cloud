package com.mty.jls.business.operation;

import com.mty.jls.business.dto.WhOrderDTO;
import com.mty.jls.business.entity.WhAccountRecords;
import com.mty.jls.business.entity.WhOrder;
import com.mty.jls.business.entity.WhProduct;
import com.mty.jls.business.enums.OperationTypeEnum;
import com.mty.jls.business.enums.OrderStateEnum;
import com.mty.jls.business.service.WhAccountRecordsService;
import com.mty.jls.business.service.WhOrderService;
import com.mty.jls.business.service.WhProductService;
import com.dove.jls.common.utils.BeanPlusUtil;
import com.dove.jls.common.utils.ValidateUtil;
import com.mty.jls.rbac.domain.SysUser;
import com.mty.jls.rbac.service.ISysUserService;
import com.mty.jls.utils.RbacUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * @author jiangpeng
 * @date 2020/10/2211:31
 */
@Component
public class WhOrderOperation {
    @Autowired
    private WhProductService productService;
    @Autowired
    private WhOrderService orderService;
    @Autowired
    private WhAccountRecordsService accountRecordsService;
    @Autowired
    private ISysUserService sysUserService;

    @Transactional(rollbackFor = Exception.class)
    public void addOrder(WhOrderDTO orderDTO) {
        final WhProduct product = productService.getByCode(orderDTO.getProductCode());
        final SysUser sysUser = sysUserService.findByUserInfoName(RbacUtil.getSecurityUser().getUsername());
        final BigDecimal totalSalePrice = orderDTO.getTotalSalePrice();

        final BigDecimal realSalePrice = product.getSalePrice().multiply(BigDecimal.valueOf(orderDTO.getSaleNum()));
        final BigDecimal totalPurchasePrice = product.getOriginalPrice().multiply(BigDecimal.valueOf(orderDTO.getSaleNum()));
        ValidateUtil.validateForResponse(realSalePrice.compareTo(totalSalePrice) != 0, "销售总价格不匹配");
        ValidateUtil.validateForResponse(totalPurchasePrice.compareTo(totalSalePrice) > 0, "销售成本不匹配");
        ValidateUtil.validateForResponse(sysUser.getBalance().compareTo(totalSalePrice) < 0, "账户余额不足");

        final WhOrder order = BeanPlusUtil.copySingleProperties(orderDTO, WhOrder::new, (s, t) -> {
            var userId = RbacUtil.getSecurityUser().getUserId();
            t.setCreator(userId);
            t.setEditor(userId);
            t.setDistributor(product.getDistributor());
            t.setProductName(product.getName());
            t.setNo(OperationTypeEnum.CONSUME.getCode() + UUID.randomUUID().toString());
            t.setTotalPurchasePrice(totalPurchasePrice);
            t.setState(OrderStateEnum.PENDING.getCode());
        });
        // 新增消费记录
        final WhAccountRecords accountRecord = buildAccountRecord(order, sysUser);
        accountRecordsService.save(accountRecord);
        // 更新账户余额
        var newBalance = sysUser.getBalance().subtract(order.getTotalSalePrice());
        sysUserService.updateById(sysUser.setBalance(newBalance));
        // 插入订单
        orderService.save(order);
    }

    private WhAccountRecords buildAccountRecord(WhOrder order, SysUser sysUser) {
        final WhAccountRecords accountRecord =
                new WhAccountRecords().setAmount(order.getTotalSalePrice()).setTradeNo(order.getNo()).setCreator(0)
                        .setOldValue(sysUser.getBalance()).setNewValue(sysUser.getBalance().subtract(order.getTotalSalePrice()))
                        .setType(OperationTypeEnum.CONSUME.getCode()).setTenantId(RbacUtil.getSecurityUser().getTenantId()).setUserId(sysUser.getUserId());
        return accountRecord;
    }
}
