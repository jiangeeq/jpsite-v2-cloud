package com.mty.jls.controller.business.operation;

import com.dove.jls.common.utils.BeanPlusUtil;
import com.dove.jls.common.utils.ValidateUtil;
import com.mty.jls.rbac.api.ISysUserService;
import com.mty.jls.rbac.bean.ISysUser;
import com.mty.jls.shop.api.IWhAccountRecordsService;
import com.mty.jls.shop.api.IWhOrderService;
import com.mty.jls.shop.api.IWhProductService;
import com.mty.jls.shop.bean.IWhAccountRecords;
import com.mty.jls.shop.bean.IWhOrder;
import com.mty.jls.shop.bean.IWhOrderDTO;
import com.mty.jls.shop.bean.IWhProduct;
import com.mty.jls.shop.enums.OperationTypeEnum;
import com.mty.jls.shop.enums.OrderStateEnum;
import com.mty.jls.utils.RbacUtil;
import org.apache.dubbo.config.annotation.Reference;
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
    @Reference(version = "1.0.0")
    private IWhProductService productService;
    @Reference(version = "1.0.0")
    private IWhOrderService orderService;
    @Reference(version = "1.0.0")
    private IWhAccountRecordsService accountRecordsService;
    @Reference(version = "1.0.0")
    private ISysUserService sysUserService;

    @Transactional(rollbackFor = Exception.class)
    public void addOrder(IWhOrderDTO orderDTO) {
        final IWhProduct product = productService.getByCode(orderDTO.getProductCode());
        final ISysUser sysUser = sysUserService.findByUserInfoName(RbacUtil.getSecurityUser().getUsername());
        final BigDecimal totalSalePrice = orderDTO.getTotalSalePrice();

        final BigDecimal realSalePrice = product.getSalePrice().multiply(BigDecimal.valueOf(orderDTO.getSaleNum()));
        final BigDecimal totalPurchasePrice = product.getOriginalPrice().multiply(BigDecimal.valueOf(orderDTO.getSaleNum()));
        ValidateUtil.validateForResponse(realSalePrice.compareTo(totalSalePrice) != 0, "销售总价格不匹配");
        ValidateUtil.validateForResponse(totalPurchasePrice.compareTo(totalSalePrice) > 0, "销售成本不匹配");
        ValidateUtil.validateForResponse(sysUser.getBalance().compareTo(totalSalePrice) < 0, "账户余额不足");

        final IWhOrder order = BeanPlusUtil.copySingleProperties(orderDTO, IWhOrder::new, (s, t) -> {
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
        final IWhAccountRecords accountRecord = buildAccountRecord(order, sysUser);
        accountRecordsService.save(accountRecord);
        // 更新账户余额
        var newBalance = sysUser.getBalance().subtract(order.getTotalSalePrice());
        sysUser.setBalance(newBalance);
        sysUserService.updateUserInfo(BeanPlusUtil.copySingleProperties(sysUser, ISysUser::new));
        // 插入订单
        orderService.save(order);
    }

    private IWhAccountRecords buildAccountRecord(IWhOrder order, ISysUser sysUser) {
        final IWhAccountRecords accountRecord =
                new IWhAccountRecords().setAmount(order.getTotalSalePrice()).setTradeNo(order.getNo()).setCreator(0)
                        .setOldValue(sysUser.getBalance()).setNewValue(sysUser.getBalance().subtract(order.getTotalSalePrice()))
                        .setType(OperationTypeEnum.CONSUME.getCode()).setTenantId(RbacUtil.getSecurityUser().getTenantId()).setUserId(sysUser.getUserId());
        return accountRecord;
    }
}
