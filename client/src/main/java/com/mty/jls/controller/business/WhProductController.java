package com.mty.jls.controller.business;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dove.jls.common.bean.PageRequest;
import com.dove.jls.common.bean.PageResponse;
import com.dove.jls.common.utils.BeanPlusUtil;
import com.dove.jls.common.utils.ValidateUtil;
import com.mty.jls.contract.model.Response;
import com.mty.jls.shop.api.IWhProductService;
import com.mty.jls.shop.bean.IWhProduct;
import com.mty.jls.shop.bean.IWhProductDTO;
import com.mty.jls.shop.bean.QueryProductReq;
import com.mty.jls.utils.RbacUtil;
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
    @Reference(version = "1.0.0", group = "shop")
    private IWhProductService productService;

    @ApiOperation("新增商品")
    @PostMapping
    public Response<String> addProduct(IWhProductDTO productDTO) {
        preCheckBusiness(productDTO);
        IWhProduct whProduct = BeanPlusUtil.copySingleProperties(productDTO, IWhProduct::new, (s, t) -> {
            var userId = RbacUtil.getSecurityUser().getUserId();
            t.setCreateor(userId);
            t.setEditor(userId);
        });
        productService.save(whProduct);
        return Response.succeed("新增商品成功");
    }

    @ApiOperation("修改商品")
    @PutMapping
    public Response<String> editProduct(IWhProductDTO productDTO) {
        preCheckBusiness(productDTO);
        IWhProduct whProduct = BeanPlusUtil.copySingleProperties(productDTO, IWhProduct::new);
        whProduct.setEditor(RbacUtil.getSecurityUser().getUserId());

        productService.updateByCode(whProduct, whProduct.getCode());
        return Response.succeed("修改商品成功");
    }

    @ApiOperation("根据商品编码商品详情")
    @GetMapping("/{code}")
    public Response<IWhProduct> getProduct(@PathVariable("code") String code) {
        final IWhProduct whProduct = productService.getByCode(code);
        ValidateUtil.validateForResponse(Objects.isNull(whProduct), "%s该商品信息不存在", code);
        return Response.succeed(whProduct);
    }

    @ApiOperation("分页查询所有商品")
    @GetMapping("/page")
    public Response pageProduct(Page page, QueryProductReq productReq) {
        final PageRequest pageRequest = new PageRequest((int) page.getCurrent(), (int) page.getSize());
        final PageResponse<List<IWhProduct>> pageResponse = productService.page(pageRequest, productReq);
        return Response.succeed(pageResponse);
    }

    @ApiOperation("查询所有商品")
    @GetMapping("/list")
    public Response listProduct(QueryProductReq productReq) {
        final List<IWhProduct> productList = productService.list(productReq);
        return Response.succeed(productList);
    }

    @DeleteMapping("/{code}")
    public Response<String> deleteProduct(@PathVariable("code") String code) {
        ValidateUtil.validateForResponse(!productService.removeByCode(code), "%s该商品信息不存在", code);
        return Response.succeed("删除成功");
    }

    private void preCheckBusiness(IWhProductDTO productDTO) {
        ValidateUtil.validateForResponse(productDTO.getOriginalPrice().compareTo(productDTO.getSalePrice()) > -1, "采购价大于等于销售价");
    }
}
