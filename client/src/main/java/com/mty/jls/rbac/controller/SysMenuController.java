package com.mty.jls.rbac.controller;


import com.mty.jls.contract.model.Response;
import com.mty.jls.contract.model.SecurityUser;
import com.mty.jls.rbac.domain.SysMenu;
import com.mty.jls.rbac.dto.MenuDTO;
import com.mty.jls.rbac.service.ISysMenuService;
import com.mty.jls.rbac.vo.MenuVo;
import com.mty.jls.utils.RbacUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@Api(value = "菜单权限表", tags = "菜单权限表")
@RestController
@RequestMapping("/menu")
public class SysMenuController {

    @Autowired
    private ISysMenuService menuService;

    @PostMapping
    @ApiOperation(value = "添加菜单")
    public Response save(@RequestBody SysMenu menu) {
        return Response.succeed(menuService.save(menu));
    }

    @GetMapping
    @ApiOperation(value = "获取菜单树")
    public Response getMenuTree() {
        SecurityUser securityUser = RbacUtil.getSecurityUser();
        return Response.succeed(menuService.selectMenuTree(securityUser.getUserId()));
    }

    @GetMapping("/getMenus")
    @ApiOperation(value = "获取所有菜单")
    public Response getMenus() {
        return Response.succeed(menuService.selectMenuTree(0));
    }

    @PutMapping
    @ApiOperation(value = "修改菜单")
    public Response updateMenu(@RequestBody MenuDTO menuDto) {
        return Response.succeed(menuService.updateMenuById(menuDto));
    }


    @DeleteMapping("/{id}")
    @ApiOperation(value = "根据id删除菜单")
    public Response deleteMenu(@PathVariable("id") Integer id) {
        return Response.succeed(menuService.removeMenuById(id));
    }

    @GetMapping("/getRouters")
    @ApiOperation(value = "获取路由")
    public Response getRouters() {
        SecurityUser securityUser = RbacUtil.getSecurityUser();
        List<MenuVo> menuVos = RbacUtil.buildMenus(menuService.selectMenuTree(securityUser.getUserId()));
        return Response.succeed(menuVos);
    }

}

