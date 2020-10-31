package com.mty.jls.rbac.controller;

import com.mty.jls.contract.model.Response;
import com.mty.jls.rbac.domain.SysDept;
import com.mty.jls.rbac.dto.DeptDTO;
import com.mty.jls.rbac.service.ISysDeptService;
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


@Api(value = "部门管理", tags = "部门管理")
@RestController
@RequestMapping("/dept")
public class SysDeptController {

    @Autowired
    private ISysDeptService deptService;

    @ApiOperation(value = "保存部门信息")
    @PostMapping
     public Response save(@RequestBody SysDept sysDept) {
        return Response.succeed(deptService.save(sysDept));
    }

    @ApiOperation(value = "查看部门信息")
    @GetMapping
     public Response getDeptList() {
        return Response.succeed(deptService.selectDeptList());
    }


    @ApiOperation(value = "获取部门树")
    @GetMapping("/tree")
     public Response getDeptTree() {
        return Response.succeed(deptService.getDeptTree());
    }


    @ApiOperation(value = "更新部门信息")
    @PutMapping
     public Response update(@RequestBody DeptDTO deptDto) {
        return Response.succeed(deptService.updateDeptById(deptDto));
    }


    @ApiOperation(value = "根据id删除部门信息")
    @DeleteMapping("/{id}")
     public Response delete(@PathVariable("id") Integer id) {
        return Response.succeed(deptService.removeById(id));
    }
}
