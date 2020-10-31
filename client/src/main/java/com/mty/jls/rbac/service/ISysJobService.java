package com.mty.jls.rbac.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mty.jls.rbac.domain.SysJob;

import java.util.List;

/**
 * <p>
 * 岗位管理 服务类
 * </p>
 *
 * @author 蒋老湿
 * @since 2019-05-01
 */
public interface ISysJobService extends IService<SysJob> {

    /**
     * 分页查询岗位列表
     * @param page
     * @param pageSize
     * @param jobName
     * @return
     */
    IPage<SysJob> selectJobList(int page, int pageSize, String jobName);


    /**
     * 根据部门id查询所属下的岗位信息
     * @param deptId
     * @return
     */
    List<SysJob> selectJobListByDeptId(Integer deptId);


    String selectJobNameByJobId(Integer jobId);

}
