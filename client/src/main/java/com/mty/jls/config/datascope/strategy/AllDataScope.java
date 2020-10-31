package com.mty.jls.config.datascope.strategy;


import com.mty.jls.contract.enums.DataScopeTypeEnum;
import com.mty.jls.rbac.domain.SysDept;
import com.mty.jls.rbac.dto.RoleDTO;
import com.mty.jls.rbac.service.ISysDeptService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private ISysDeptService deptService;


    @Override
    public List<Integer> getDeptIds(RoleDTO roleDto, DataScopeTypeEnum dataScopeTypeEnum) {
        List<SysDept> sysDepts = deptService.list();
        return sysDepts.stream().map(SysDept::getDeptId).collect(Collectors.toList());
    }
}
