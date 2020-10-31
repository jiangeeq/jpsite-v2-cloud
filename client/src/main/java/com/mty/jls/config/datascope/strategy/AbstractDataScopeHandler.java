package com.mty.jls.config.datascope.strategy;



import com.mty.jls.contract.enums.DataScopeTypeEnum;
import com.mty.jls.rbac.dto.RoleDTO;

import java.util.List;

/**
 * @Classname AbstractDataScopeHandler
 * @Description 创建抽象策略角色 主要作用 数据权限范围使用
 * @Author Created by 蒋老湿
 * @Date 2019-06-08 15:45
 * @Version 1.0
 */

public interface AbstractDataScopeHandler {

    /**
     * @param roleDto
     * @param dataScopeTypeEnum
     * @return
     */
    List<Integer> getDeptIds(RoleDTO roleDto, DataScopeTypeEnum dataScopeTypeEnum);
}
