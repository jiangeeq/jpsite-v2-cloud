package com.mty.jls.rbac.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dove.jls.common.utils.BeanPlusUtil;
import com.mty.jls.rbac.api.ISysDeptService;
import com.mty.jls.rbac.api.ISysJobService;
import com.mty.jls.rbac.bean.IPageResponse;
import com.mty.jls.rbac.bean.ISysJob;
import com.mty.jls.rbac.domain.SysJob;
import com.mty.jls.rbac.mapper.SysJobMapper;
import org.apache.dubbo.config.annotation.Service;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service(cluster = "failsafe",
        loadbalance = "roundrobin",
        version = "1.0.0"
)
public class SysJobServiceImpl extends ServiceImpl<SysJobMapper, SysJob> implements ISysJobService {

    @Autowired
    private ISysDeptService deptService;

    @Override
    public IPageResponse<List<ISysJob>> selectJobList(int page, int pageSize, String jobName) {
        LambdaQueryWrapper<SysJob> jobLambdaQueryWrapper = Wrappers.<SysJob>lambdaQuery();
        if (Strings.isNotEmpty(jobName)) {
            jobLambdaQueryWrapper.eq(SysJob::getJobName, jobName);
        }
        IPage<SysJob> sysJobPage = baseMapper.selectPage(new Page<>(page, pageSize), jobLambdaQueryWrapper);
        List<ISysJob> recoreds = null;
        if (CollectionUtil.isNotEmpty(sysJobPage.getRecords())) {
            // 按照sort排序
            List<SysJob> collect =
                    sysJobPage.getRecords().stream().peek(sysJob -> sysJob.setDeptName(deptService.selectDeptNameByDeptId(sysJob.getDeptId())))
                    .sorted(Comparator.comparingInt(SysJob::getSort)).collect(Collectors.toList());
            recoreds = BeanPlusUtil.copyListProperties(collect, ISysJob::new);
        }
        IPageResponse<List<ISysJob>> iPageResponse = IPageResponse.ok();
        iPageResponse.setPage(sysJobPage.getPages()).setPageSize(sysJobPage.getSize())
                .setTotal(sysJobPage.getTotal()).setRecords(recoreds);
        return iPageResponse;
    }

    @Override
    public List<ISysJob> selectJobListByDeptId(Integer deptId) {
        var list = baseMapper.selectList(Wrappers.<SysJob>lambdaQuery().select(SysJob::getId, SysJob::getJobName).eq(SysJob::getDeptId, deptId));
        return BeanPlusUtil.copyListProperties(list, ISysJob::new);
    }

    @Override
    public String selectJobNameByJobId(Integer jobId) {
        return baseMapper.selectOne(Wrappers.<SysJob>lambdaQuery().select(SysJob::getJobName).eq(SysJob::getId, jobId)).getJobName();
    }

}
