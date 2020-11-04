package com.mty.jls.rbac.api;


import com.mty.jls.rbac.bean.IPageResponse;
import com.mty.jls.rbac.bean.ISysLog;

import java.util.List;

/**
 * <p>
 * 系统日志 服务类
 * </p>
 *
 * @author 蒋老湿
 * @since 2019-04-27
 */
public interface ISysLogService {

    /**
     * 分页查询日志
     * @param page
     * @param pageSize
     * @param type
     * @return
     */
    IPageResponse<List<ISysLog>> selectLogList(Integer page, Integer pageSize, Integer type, String userName);


    Boolean removeById(Integer id);
}
