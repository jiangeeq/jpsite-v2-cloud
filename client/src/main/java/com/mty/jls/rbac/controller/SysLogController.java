package com.mty.jls.rbac.controller;

import com.mty.jls.contract.model.Response;
import com.mty.jls.rbac.service.ISysLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Api(value = "系统日志", tags = "系统日志")
@RestController
@RequestMapping("/log")
public class SysLogController {

    @Resource
    private ISysLogService logService;

    @ApiOperation(value = "分页查询日志列表")
    @GetMapping
     public Response selectLog(@RequestParam("page") Integer page, @RequestParam("pageSize") Integer pageSize, @RequestParam("type") Integer type,
                       @RequestParam String userName) {
        return Response.succeed(logService.selectLogList(page, pageSize, type, userName));
    }

    @ApiOperation(value = "删除日志")
    @DeleteMapping("/{logId}")
     public Response delete(@PathVariable("logId") Integer logId) {
        return Response.succeed(logService.removeById(logId));
    }
}

