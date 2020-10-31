package com.mty.jls.rbac.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mty.jls.rbac.domain.SysLog;

/**
 * <p>
 * 系统日志 服务类
 * </p>
 *
 * @author 蒋老湿
 * @since 2019-04-27
 */
public interface ISysLogService extends IService<SysLog> {

    /**
     * 分页查询日志
     * @param page
     * @param pageSize
     * @param type
     * @return
     */
    IPage<SysLog> selectLogList(Integer page, Integer pageSize, Integer type, String userName);



}
