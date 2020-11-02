package com.mty.jls.business.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mty.jls.business.controller.reqParam.QueryProductReq;
import com.mty.jls.business.dto.WhProductDTO;
import com.mty.jls.business.entity.WhProduct;
import com.mty.jls.business.operation.WhProductOperation;
import com.mty.jls.business.service.WhProductService;
import com.mty.jls.contract.model.Response;
import com.dove.jls.common.utils.BeanPlusUtil;
import com.dove.jls.common.utils.ValidateUtil;
import com.mty.jls.utils.RbacUtil;
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

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 商品信息表 前端控制器
 * </p>
 *
 * @author 掘金-蒋老湿（公众号：十分钟学编程）
 * @since 2020-10-11
 */
@Api(value = "商品管理", tags = "商品管理")
@RestController
@RequestMapping("/whProduct")
public class WhProductController {
    @Autowired
    private WhProductService productService;
    @Autowired
    private WhProductOperation productOperation;

    @ApiOperation("新增商品")
    @PostMapping
    public Response<String> addProduct(@Valid WhProductDTO productDTO) {
        preCheckBusiness(productDTO);
        WhProduct whProduct = BeanPlusUtil.copySingleProperties(productDTO, WhProduct::new, (s, t) -> {
            var userId = RbacUtil.getSecurityUser().getUserId();
            t.setCreateor(userId);
            t.setEditor(userId);
        });
        productService.save(whProduct);
        return Response.succeed("新增商品成功");
    }

    @ApiOperation("修改商品")
    @PutMapping
    public Response<String> editProduct(@Valid WhProductDTO productDTO) {
        preCheckBusiness(productDTO);
        WhProduct whProduct = BeanPlusUtil.copySingleProperties(productDTO, WhProduct::new);
        whProduct.setEditor(RbacUtil.getSecurityUser().getUserId());

        LambdaQueryWrapper<WhProduct> queryWrapper = Wrappers.<WhProduct>lambdaQuery().eq(WhProduct::getCode, whProduct.getCode());
        productService.update(whProduct, queryWrapper);
        return Response.succeed("修改商品成功");
    }

    @ApiOperation("根据商品编码商品详情")
    @GetMapping("/{code}")
    public Response<WhProduct> getProduct(@PathVariable("code") String code) {
        LambdaQueryWrapper<WhProduct> queryWrapper = Wrappers.<WhProduct>lambdaQuery()
                .select(WhProduct::getId, WhProduct::getCode, WhProduct::getName, WhProduct::getSalePrice, WhProduct::getCategory,
                        WhProduct::getDescription, WhProduct::getDistributor, WhProduct::getState, WhProduct::getMinnumOrder,
                        WhProduct::getMaxnumOrder, WhProduct::getOrderMultiple, WhProduct::getCategoryCode)
                .eq(WhProduct::getCode, code);
        final WhProduct whProduct = productService.getOne(queryWrapper);

        ValidateUtil.validateForResponse(Objects.isNull(whProduct), "%s该商品信息不存在", code);

        return Response.succeed(whProduct);
    }

    @ApiOperation("分页查询所有商品")
    @GetMapping("/page")
    public Response<Page<WhProduct>> pageProduct(Page page, QueryProductReq productReq) {
        LambdaQueryWrapper queryWrapper = productOperation.queryFieldWrapper(productReq);
        final Page<WhProduct> pageResult = productService.page(page, queryWrapper);

        return Response.succeed(pageResult);
    }
    @ApiOperation("查询所有商品")
    @GetMapping("/list")
    public Response listProduct(QueryProductReq productReq) {
        LambdaQueryWrapper queryWrapper = productOperation.queryFieldWrapper(productReq);
        final List productList = productService.list(queryWrapper);

        return Response.succeed(productList);
    }

    @DeleteMapping("/{code}")
    public Response<String> deleteProduct(@PathVariable("code") String code) {
        LambdaQueryWrapper<WhProduct> queryWrapper = Wrappers.<WhProduct>lambdaQuery().eq(WhProduct::getCode, code);
        ValidateUtil.validateForResponse(!productService.remove(queryWrapper), "%s该商品信息不存在", code);
        return Response.succeed("删除成功");
    }

    private void preCheckBusiness(WhProductDTO productDTO) {
        ValidateUtil.validateForResponse(productDTO.getOriginalPrice().compareTo(productDTO.getSalePrice()) > -1, "采购价大于等于销售价");
    }
}
