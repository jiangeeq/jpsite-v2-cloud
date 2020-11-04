package com.mty.jls.config.datascope.strategy;


import com.mty.jls.contract.enums.DataScopeTypeEnum;
import com.mty.jls.rbac.api.ISysDeptService;
import com.mty.jls.rbac.bean.IRoleDTO;
import com.mty.jls.rbac.bean.ISysDept;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Classname AllDataScope
 * @Description 所有
 * @Author Created by 蒋老湿
 * @Date 2019-06-08 16:27
 * @Version 1.0
 */
@Component("1")
public class AllDataScope implements AbstractDataScopeHandler{

    @Reference(version = "1.0.0")
    private ISysDeptService deptService;


    @Override
    public List<Integer> getDeptIds(IRoleDTO roleDto, DataScopeTypeEnum dataScopeTypeEnum) {
        List<ISysDept> sysDepts = deptService.selectDeptList();
        return sysDepts.stream().map(ISysDept::getDeptId).collect(Collectors.toList());
    }
}
