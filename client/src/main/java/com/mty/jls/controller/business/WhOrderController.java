package com.mty.jls.controller.business;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dove.jls.common.bean.PageRequest;
import com.dove.jls.common.bean.PageResponse;
import com.mty.jls.contract.model.Response;
import com.mty.jls.controller.business.operation.WhOrderOperation;
import com.mty.jls.shop.api.IWhOrderService;
import com.mty.jls.shop.bean.IWhOrder;
import com.mty.jls.shop.bean.IWhOrderDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

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
    @Reference(version = "1.0.0")
    private IWhOrderService orderService;
    @Resource
    private WhOrderOperation orderOperation;

    @ApiOperation("创建订单")
    @PostMapping
    public Response addOrder(IWhOrderDTO orderDTO) {
        orderOperation.addOrder(orderDTO);
        return Response.succeed();
    }

    @ApiOperation("删除订单")
    @DeleteMapping
    public Response delete(Integer id) {
        final IWhOrder whOrder = orderService.getById(id).setDelFlag(true);
        orderService.updateById(whOrder);
        return Response.succeed();
    }

    @ApiOperation("修改订单状态")
    @PutMapping("/updateState")
    public Response updateOrderState(Integer id, Integer state) {
        final IWhOrder whOrder = orderService.getById(id).setState(state);
        orderService.updateById(whOrder);
        return Response.succeed();
    }

    @ApiOperation("查询订单列表（分页）")
    @GetMapping("/page")
    public Response pageOrder(Page page, IWhOrderDTO orderDTO) {
        final PageRequest pageRequest = new PageRequest((int) page.getCurrent(), (int) page.getSize());
        final PageResponse pageResponse = orderService.page(pageRequest, orderDTO);
        return Response.succeed(pageResponse);
    }

    @ApiOperation("查询订单详情")
    @GetMapping("/{id}")
    public Response orderInfo(@PathVariable("id") Integer id) {
        final IWhOrder order = orderService.getById(id);
        return Response.succeed(order);
    }
}

