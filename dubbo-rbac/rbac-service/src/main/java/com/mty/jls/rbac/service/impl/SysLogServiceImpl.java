package com.mty.jls.rbac.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mty.jls.rbac.api.ISysLogService;
import com.mty.jls.rbac.mapper.SysLogMapper;
import com.mty.jls.rbac.domain.SysLog;
import org.apache.dubbo.config.annotation.Service;


@Service(cluster = "failsafe",
        loadbalance = "roundrobin",
        version = "1.0.0"
)
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
