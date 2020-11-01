package com.mty.rbac.api;


import com.mty.rbac.bean.ISysJob;

import java.util.List;

/**
 * <p>
 * 岗位管理 服务类
 * </p>
 *
 * @author 蒋老湿
 * @since 2019-05-01
 */
public interface ISysJobService {

    /**
     * 分页查询岗位列表
     * @param page
     * @param pageSize
     * @param jobName
     * @return
     */
//    IPage<SysJob> selectJobList(int page, int pageSize, String jobName);


    /**
     * 根据部门id查询所属下的岗位信息
     * @param deptId
     * @return
     */
    List<ISysJob> selectJobListByDeptId(Integer deptId);


    String selectJobNameByJobId(Integer jobId);

}
