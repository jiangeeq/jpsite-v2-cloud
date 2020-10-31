package com.mty.jls.rbac.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mty.jls.rbac.domain.SysTenant;

import java.util.List;

/**
 * <p>
 * 租户表 服务类
 * </p>
 *
 * @author 蒋老湿
 * @since 2019-08-10
 */
public interface ISysTenantService extends IService<SysTenant> {

    /**
     * 保存租户
     *
     * @param sysTenant
     * @return
     */
    boolean saveTenant(SysTenant sysTenant);


    /**
     * 获取正常租户
     *
     * @return
     */
    List<SysTenant> getNormalTenant();
}
