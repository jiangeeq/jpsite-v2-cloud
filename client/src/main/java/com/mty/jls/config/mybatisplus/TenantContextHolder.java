package com.mty.jls.config.mybatisplus;

import com.google.common.collect.Maps;
import lombok.experimental.UtilityClass;

import java.util.Map;

/**
 * @Classname PreTenantContext
 * @Description 多租户上下文
 */
@UtilityClass
public class TenantContextHolder {
    private final String KEY_CURRENT_TENANT_ID = "KEY_CURRENT_TENANT_ID";
    private final Map<String, Object> PRE_TENANT_CONTEXT = Maps.newConcurrentMap();

    /**
     * 设置租户Id
     *
     * @param tenantId 租户Id
     */
    public void setCurrentTenantId(Long tenantId) {
        PRE_TENANT_CONTEXT.put(KEY_CURRENT_TENANT_ID, tenantId);
    }

    /**
     * 获取租户Id
     *
     * @return
     */
    public Long getCurrentTenantId() {
        return (Long) PRE_TENANT_CONTEXT.get(KEY_CURRENT_TENANT_ID);
    }

    /**
     * 清除租户信息
     */
    public void clear() {
        PRE_TENANT_CONTEXT.clear();
    }
}
