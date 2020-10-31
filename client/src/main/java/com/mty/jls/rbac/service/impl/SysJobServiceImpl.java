package com.mty.jls.rbac.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mty.jls.rbac.domain.SysJob;
import com.mty.jls.rbac.mapper.SysJobMapper;
import com.mty.jls.rbac.service.ISysDeptService;
import com.mty.jls.rbac.service.ISysJobService;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SysJobServiceImpl extends ServiceImpl<SysJobMapper, SysJob> implements ISysJobService {

    @Autowired
    private ISysDeptService deptService;

    @Override
    public IPage<SysJob> selectJobList(int page, int pageSize, String jobName) {
        LambdaQueryWrapper<SysJob> jobLambdaQueryWrapper = Wrappers.<SysJob>lambdaQuery();
        if (Strings.isNotEmpty(jobName)) {
            jobLambdaQueryWrapper.eq(SysJob::getJobName, jobName);
        }
        IPage<SysJob> sysJobPage = baseMapper.selectPage(new Page<>(page, pageSize), jobLambdaQueryWrapper);
        List<SysJob> sysJobList = sysJobPage.getRecords();
        if (CollectionUtil.isNotEmpty(sysJobList)) {
            // 按照sort排序
            List<SysJob> collect = sysJobList.stream().peek(sysJob -> sysJob.setDeptName(deptService.selectDeptNameByDeptId(sysJob.getDeptId())))
                    .sorted(Comparator.comparingInt(SysJob::getSort)).collect(Collectors.toList());
            return sysJobPage.setRecords(collect);
        }
        return sysJobPage;
    }

    @Override
    public List<SysJob> selectJobListByDeptId(Integer deptId) {
        return baseMapper.selectList(Wrappers.<SysJob>lambdaQuery().select(SysJob::getId, SysJob::getJobName).eq(SysJob::getDeptId, deptId));
    }

    @Override
    public String selectJobNameByJobId(Integer jobId) {
        return baseMapper.selectOne(Wrappers.<SysJob>lambdaQuery().select(SysJob::getJobName).eq(SysJob::getId, jobId)).getJobName();
    }

}
