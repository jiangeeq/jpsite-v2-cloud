package com.mty.jls.controller.rbac;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dove.jls.common.bean.PageRequest;
import com.mty.jls.contract.model.Response;

import com.mty.jls.rbac.api.ISysTenantService;
import com.mty.jls.rbac.bean.ISysTenant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "租户管理", tags = "租户管理")
@RestController
@RequestMapping("/tenant")
public class SysTenantController {

    @Reference(version = "1.0.0", group = "rbac")
    private ISysTenantService sysTenantService;

    @GetMapping("/page")
    @ApiOperation(value = "分页查询")
     public Response getSysTenantPage(Page page, ISysTenant sysTenant) {
        var pageRequest = new PageRequest((int)page.getCurrent(), (int)page.getSize());
        return Response.succeed(sysTenantService.page(pageRequest, sysTenant));
    }


    @GetMapping("/list")
    @ApiOperation(value = "查询全部有效的租户")
     public Response list() {
        return Response.succeed(sysTenantService.getNormalTenant());
    }

    @ApiOperation(value = "新增租户")
    @PostMapping
     public Response save(@RequestBody ISysTenant sysTenant) {
        return Response.succeed(sysTenantService.saveTenant(sysTenant));
    }


    @ApiOperation(value = "修改租户")
    @PutMapping
     public Response updateById(@RequestBody ISysTenant sysTenant) {
        return Response.succeed(sysTenantService.updateById(sysTenant));
    }

    @ApiOperation(value = "通过id删除租户")
    @DeleteMapping("/{id}")
     public Response removeById(@PathVariable Integer id) {
        return Response.succeed(sysTenantService.removeById(id));
    }
}
