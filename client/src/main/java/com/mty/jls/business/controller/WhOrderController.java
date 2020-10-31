package com.mty.jls.business.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mty.jls.business.dto.WhOrderDTO;
import com.mty.jls.business.entity.WhOrder;
import com.mty.jls.business.operation.WhOrderOperation;
import com.mty.jls.business.service.WhOrderService;
import com.mty.jls.contract.model.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 销售订单表 前端控制器
 * </p>
 *
 * @author 掘金-蒋老湿（公众号：十分钟学编程）
 * @since 2020-10-22
 */
@Api(value = "订单管理", tags = "订单管理")
@RestController
@RequestMapping("/whOrder")
public class WhOrderController {
    @Autowired
    private WhOrderOperation orderOperation;
    @Autowired
    private WhOrderService orderService;


    @ApiOperation("创建订单")
    @PostMapping
    public Response addOrder(WhOrderDTO orderDTO) {
        orderOperation.addOrder(orderDTO);
        return Response.succeed();
    }

    @ApiOperation("删除订单")
    @DeleteMapping
    public Response delete(Integer id) {
        final WhOrder whOrder = orderService.getById(id).setDelFlag(true);
        orderService.updateById(whOrder);
        return Response.succeed();
    }

    @ApiOperation("修改订单状态")
    @PutMapping("/updateState")
    public Response updateOrderState(Integer id, Integer state) {
        final WhOrder whOrder = orderService.getById(id).setState(state);
        orderService.updateById(whOrder);
        return Response.succeed();
    }

    @ApiOperation("查询订单列表（分页）")
    @GetMapping("/page")
    public Response pageOrder(Page page, WhOrderDTO orderDTO) {
        final LambdaQueryWrapper<WhOrder> queryWrapper = Wrappers.<WhOrder>lambdaQuery().eq(WhOrder::getProductCode, orderDTO.getProductCode())
                .or()
                .eq(WhOrder::getProductName, orderDTO.getProductName())
                .or()
                .between(WhOrder::getCreateTime, orderDTO.getMinCreateTime(), orderDTO.getMaxCreateTime())
                .or()
                .between(WhOrder::getTotalSalePrice, orderDTO.getMinSalePrice(), orderDTO.getMaxSalePrice());

        final Page pageResult = orderService.page(page, queryWrapper);
        return Response.succeed(pageResult);
    }

    @ApiOperation("查询订单详情")
    @GetMapping("/{id}")
    public Response orderInfo(@PathVariable("id") Integer id) {
        final WhOrder order = orderService.getById(id);
        return Response.succeed(order);
    }
}

