package com.mty.jls.config.mybatisplus;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @Classname PreTenantConfigProperties
 * @Description 多租户动态配置
 */
@Data
@Component
@ConfigurationProperties(prefix = "mybatis.tenant")
public class TenantConfigProperties {

    /**
     * 维护租户id
     */
    private String tenantIdColumn = "tenant_id";

    /**
     * 多租户的数据表集合
     */
    private List<String> ignoreTenantTables = new ArrayList<>();
}
