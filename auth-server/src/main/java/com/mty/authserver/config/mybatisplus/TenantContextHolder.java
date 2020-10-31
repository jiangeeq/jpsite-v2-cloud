package com.mty.authserver.config.mybatisplus;

import lombok.experimental.UtilityClass;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author jiangpeng
 * @Classname PreTenantContext
 * @Description 多租户上下文
 */
@UtilityClass
public class TenantContextHolder {
    private final String KEY_CURRENT_TENANT_ID = "KEY_CURRENT_TENANT_ID";
    private final Map<String, Object> PRE_TENANT_CONTEXT = new ConcurrentHashMap<>();

    /**
     * 设置租户Id
     * @param providerId
     */
    public void setCurrentTenantId(Long providerId) {
        PRE_TENANT_CONTEXT.put(KEY_CURRENT_TENANT_ID, providerId);
    }

    /**
     * 获取租户Id
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
