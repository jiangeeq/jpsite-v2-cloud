package com.mty.jls.rbac.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mty.rbac.domain.SysLog;
import com.mty.rbac.mapper.SysLogMapper;
import com.mty.rbac.api.ISysLogService;
import org.apache.dubbo.config.annotation.Service;


@Service
public class SysLogServiceImpl extends ServiceImpl<SysLogMapper, SysLog> implements ISysLogService {

//    @Override
//    public IPage<SysLog> selectLogList(Integer page, Integer pageSize, Integer type, String userName) {
//        Page<SysLog> logPage = new Page<>(page, pageSize);
//        LambdaQueryWrapper<SysLog> sysLogLambdaQueryWrapper = Wrappers.<SysLog>lambdaQuery().eq(SysLog::getType, type).orderByDesc(SysLog::getStartTime);
//        if (StrUtil.isNotEmpty(userName)) {
//            sysLogLambdaQueryWrapper.like(SysLog::getUserName, userName);
//        }
//        return baseMapper.selectPage(logPage, sysLogLambdaQueryWrapper);
//    }
}
