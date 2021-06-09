package com.mty.jls.rbac.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dove.jls.common.utils.BeanPlusUtil;
import com.mty.jls.rbac.api.ISysLogService;
import com.mty.jls.rbac.bean.IPageResponse;
import com.mty.jls.rbac.bean.ISysLog;
import com.mty.jls.rbac.mapper.SysLogMapper;
import com.mty.jls.rbac.domain.SysLog;
import org.apache.dubbo.config.annotation.Service;

import java.util.List;


@Service(cluster = "failsafe",
        loadbalance = "roundrobin",
        group = "rbac",
        version = "1.0.0"
)
public class SysLogServiceImpl extends ServiceImpl<SysLogMapper, SysLog> implements ISysLogService {

    @Override
    public IPageResponse<List<ISysLog>> selectLogList(Integer page, Integer pageSize, Integer type, String userName) {
        Page<SysLog> logPage = new Page<>(page, pageSize);
        LambdaQueryWrapper<SysLog> sysLogLambdaQueryWrapper =
                Wrappers.<SysLog>lambdaQuery().eq(SysLog::getType, type).orderByDesc(SysLog::getStartTime);
        if (StrUtil.isNotEmpty(userName)) {
            sysLogLambdaQueryWrapper.like(SysLog::getUserName, userName);
        }
        var sysLogPage = baseMapper.selectPage(logPage, sysLogLambdaQueryWrapper);
        final List<ISysLog> records = BeanPlusUtil.copyListProperties(sysLogPage.getRecords(), ISysLog::new);

        final IPageResponse<List<ISysLog>> pageResponse = IPageResponse.ok();
        pageResponse.setPage(sysLogPage.getCurrent()).setPageSize(sysLogPage.getSize()).setTotal(sysLogPage.getTotal()).setRecords(records);
        return pageResponse;
    }

    @Override
    public Boolean removeById(Integer id) {
        return baseMapper.deleteById(id) > 0;
    }
}
