package com.mty.jls.rbac.controller;

import com.mty.jls.contract.model.Response;
import com.mty.jls.rbac.dto.RoleDTO;
import com.mty.jls.rbac.service.ISysRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Api(value = "系统角色表", tags = "系统角色表")
@RestController
@RequestMapping("/role")
public class SysRoleController {

    @Resource
    private ISysRoleService roleService;


    @ApiOperation(value = "获取角色列表")
    @GetMapping
     public Response getRoleList(@RequestParam String roleName) {
        return Response.succeed(roleService.selectRoleList(roleName));
    }


    @ApiOperation(value = "保存角色以及菜单权限")
    @PostMapping
     public Response save(@RequestBody RoleDTO roleDto) {
        return Response.succeed(roleService.saveRoleMenu(roleDto));
    }

    @ApiOperation(value = "根据角色id获取菜单")
    @GetMapping("/findRoleMenus/{roleId}")
     public Response findRoleMenus(@PathVariable("roleId") Integer roleId) {
        return Response.succeed(roleService.findMenuListByRoleId(roleId));
    }


    @ApiOperation(value = "更新角色以及菜单权限")
    @PutMapping
     public Response update(@RequestBody RoleDTO roleDto) {
        return Response.succeed(roleService.updateRoleMenu(roleDto));
    }


    @ApiOperation(value = "删除角色以及权限")
    @DeleteMapping("/{roleId}")
     public Response delete(@PathVariable("roleId") Integer roleId) {
        return Response.succeed(roleService.removeById(roleId));
    }
}
