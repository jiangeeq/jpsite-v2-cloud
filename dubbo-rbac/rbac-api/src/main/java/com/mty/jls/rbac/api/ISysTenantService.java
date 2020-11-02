package com.mty.jls.rbac.api;


import com.mty.jls.rbac.bean.ISysTenant;

import java.util.List;

/**
 * <p>
 * 租户表 服务类
 * </p>
 *
 * @author 蒋老湿
 * @since 2019-08-10
 */
public interface ISysTenantService {

    /**
     * 保存租户
     *
     * @param sysTenant
     * @return
     */
    boolean saveTenant(ISysTenant sysTenant);


    /**
     * 获取正常租户
     *
     * @return
     */
    List<ISysTenant> getNormalTenant();
}
