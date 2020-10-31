package com.mty.jls.rbac.controller;

import com.mty.jls.contract.model.Response;
import com.mty.jls.rbac.domain.SysDict;
import com.mty.jls.rbac.dto.DictDTO;
import com.mty.jls.rbac.service.ISysDictService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@Api(value = "字典表", tags = "字典表")
@RestController
@RequestMapping("/dict")
public class SysDictController {

    @Autowired
    private ISysDictService dictService;

    @ApiOperation(value = "保存字典信息")
    @PostMapping
    public Response save(@RequestBody SysDict sysDict) {
        return Response.succeed(dictService.save(sysDict));
    }

    @ApiOperation(value = "获取字典列表集合")
    @GetMapping
    public Response getList(Integer page, Integer pageSize) {
        return Response.succeed(dictService.selectDictList(page, pageSize));
    }

    @ApiOperation(value = "获取字典详情列表集合")
    @GetMapping("/getDictDetailList")
    public Response selectDictDetailList(@RequestParam String name) {
        return Response.succeed(dictService.selectDictDetailList(name));
    }


    @ApiOperation(value = "更新字典")
    @PutMapping
    public Response update(@RequestBody DictDTO dictDto) {
        return Response.succeed(dictService.updateDict(dictDto));
    }

    @ApiOperation(value = "根据id删除字典")
    @DeleteMapping("{id}")
    public Response delete(@PathVariable("id") int id) {
        return Response.succeed(dictService.removeById(id));
    }

    @ApiOperation(value = "根据name删除字典")
    @DeleteMapping("/delete")
    public Response deleteName(@RequestParam String name) {
        return Response.succeed(dictService.deleteDictByName(name));
    }
}

