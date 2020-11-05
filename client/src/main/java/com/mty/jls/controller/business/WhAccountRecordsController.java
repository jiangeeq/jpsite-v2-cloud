package com.mty.jls.controller.business;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dove.jls.common.bean.PageRequest;
import com.dove.jls.common.bean.PageResponse;
import com.mty.jls.contract.model.Response;
import com.mty.jls.shop.api.IWhAccountRecordsService;
import com.mty.jls.shop.bean.IWhAccountRecordDTO;
import com.mty.jls.shop.bean.IWhAccountRecords;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
    @Reference(version = "1.0.0")
    private IWhAccountRecordsService accountRecordsService;

    @ApiOperation("查询单个消费记录")
    @GetMapping("/{id}")
    public Response getRecord(@PathVariable("id") Integer id) {
        final IWhAccountRecords accountRecord = accountRecordsService.getById(id);
        return Response.succeed(accountRecord);
    }

    @ApiOperation("查询多条消费记录（分页）")
    @GetMapping("/page")
    public Response getRecord(Page page, IWhAccountRecordDTO accountRecordDTO) {
        final PageRequest pageRequest = new PageRequest((int) page.getCurrent(), (int) page.getSize());
        final PageResponse<List<IWhAccountRecords>> pageResponse = accountRecordsService.page(pageRequest, accountRecordDTO);
        return Response.succeed(pageResponse);
    }
}

