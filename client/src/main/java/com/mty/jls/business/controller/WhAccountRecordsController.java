package com.mty.jls.business.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mty.jls.business.dto.WhAccountRecordDTO;
import com.mty.jls.business.entity.WhAccountRecords;
import com.mty.jls.business.service.WhAccountRecordsService;
import com.mty.jls.contract.model.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 账号充值消费记录 前端控制器
 * </p>
 *
 * @author 掘金-蒋老湿（公众号：十分钟学编程）
 * @since 2020-10-22
 */
@Api(value = "账号充值消费记录", tags = "账号充值消费记录")
@RestController
@RequestMapping("/whAccountRecords")
public class WhAccountRecordsController {
    @Autowired
    private WhAccountRecordsService accountRecordsService;

    @ApiOperation("查询单个消费记录")
    @GetMapping("/{id}")
    public Response getRecord(@PathVariable("id") Integer id) {
        final WhAccountRecords accountRecord = accountRecordsService.getById(id);
        return Response.succeed(accountRecord);
    }

    @ApiOperation("查询多条消费记录（分页）")
    @GetMapping("/page")
    public Response getRecord(Page page, WhAccountRecordDTO accountRecordDTO) {
        final LambdaQueryWrapper<WhAccountRecords> queryWrapper = Wrappers.<WhAccountRecords>lambdaQuery().between(WhAccountRecords::getCreateTime,
                accountRecordDTO.getMinCreateTime(), accountRecordDTO.getMaxCreateTime())
                .or()
                .between(WhAccountRecords::getAmount, accountRecordDTO.getMinAmount(), accountRecordDTO.getMaxAmount())
                .or()
                .eq(WhAccountRecords::getType, accountRecordDTO.getType());
        Page pageResult = accountRecordsService.page(page, queryWrapper);
        return Response.succeed(pageResult);
    }
}

