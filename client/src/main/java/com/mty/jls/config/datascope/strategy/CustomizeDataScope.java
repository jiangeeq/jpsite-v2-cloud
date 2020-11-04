package com.mty.jls.config.datascope.strategy;


import com.mty.jls.contract.enums.DataScopeTypeEnum;
import com.mty.jls.rbac.api.ISysDeptService;
import com.mty.jls.rbac.bean.IRoleDTO;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Classname CustomizeDataScope
 * @Description 自定义
 * @Author Created by 蒋老湿
 * @Date 2019-06-08 16:31
 * @Version 1.0
 */
@Component("4")
public class CustomizeDataScope implements AbstractDataScopeHandler {

    @Reference(version = "1.0.0")
    private ISysDeptService deptService;

    @Override
    public List<Integer> getDeptIds(IRoleDTO roleDto, DataScopeTypeEnum dataScopeTypeEnum) {
        List<Integer> roleDeptIds = roleDto.getRoleDeptIds();
        List<Integer> ids = new ArrayList<>();
        for (Integer deptId : roleDeptIds) {
            ids.addAll(deptService.selectDeptIds(deptId));
        }
        Set<Integer> set = new HashSet<>(ids);
        ids.clear();
        ids.addAll(set);
        return ids;
    }
}
