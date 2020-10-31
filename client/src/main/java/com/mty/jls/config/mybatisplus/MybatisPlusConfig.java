package com.mty.jls.config.mybatisplus;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import com.mty.jls.config.datascope.DataScopeInterceptor;
import com.mty.jls.contract.exception.BusinessException;
import com.mty.jls.utils.RbacUtil;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Objects;

/**
 * @author 掘金-蒋老湿（公众号：十分钟学编程）
 * @date 2018/11/30
 */
@EnableTransactionManagement
@Configuration
@Slf4j
@MapperScan({"com.mty.jls.rbac.mapper", "com.mty.jls.business.dao", "com.mty.jls.iflytek.dao"})
public class MybatisPlusConfig {
    @Autowired
    private TenantConfigProperties configProperties;

    /**
     * 新的分页插件,一缓和二缓遵循mybatis的规则,需要设置 MybatisConfiguration#useDeprecatedExecutor = false 避免缓存出现问题(该属性会在旧插件移除后一同移除)
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(paginationInterceptor());
        interceptor.addInnerInterceptor(new TenantLineInnerInterceptor(new TenantLineHandler() {
            @Override
            public Expression getTenantId() {
                // select since: 3.3.2，参数 true 表示为 select 下的 where 条件,false 表示 insert/update/delete 下的条件
                // 只有 select 下才允许多参(ValueListExpression),否则只支持单参
                Long tenantId = 0L;
                if (Objects.nonNull(TenantContextHolder.getCurrentTenantId()) && TenantContextHolder.getCurrentTenantId() > 0) {
                    tenantId = TenantContextHolder.getCurrentTenantId();
                    TenantContextHolder.clear();
                } else {
                    try {
                        tenantId = RbacUtil.getSecurityUser().getTenantId();
                    } catch (Exception e) {
                        log.error("mybatisPlusInterceptor 没有获取到登录人的tenantId, 可能是登录成功前调用了sql操作", e);
                    }
                }
                log.debug("====>当前租户为[{}]", tenantId);

                return new LongValue(tenantId);
            }

            @Override
            public String getTenantIdColumn() {
                return configProperties.getTenantIdColumn();
            }

            @Override
            public boolean ignoreTable(String tableName) {
                return configProperties.getIgnoreTenantTables().stream().anyMatch(e -> e.equalsIgnoreCase(tableName));
            }
        }));
        return interceptor;
    }

    /**
     * 分页插件
     */
    @Bean
    public PaginationInnerInterceptor paginationInterceptor() {
        PaginationInnerInterceptor paginationInterceptor = new PaginationInnerInterceptor();
        return paginationInterceptor;
    }

    /**
     * 数据权限插件
     *
     * @param dataSource 数据源
     * @return DataScopeInterceptor
     */
    @Bean
    @ConditionalOnMissingBean
    public DataScopeInterceptor dataScopeInterceptor(DataSource dataSource) {
        return new DataScopeInterceptor(dataSource);
    }
}
