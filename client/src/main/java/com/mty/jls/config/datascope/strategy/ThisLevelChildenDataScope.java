package com.mty.jls.config.datascope.strategy;


import com.mty.jls.contract.enums.DataScopeTypeEnum;
import com.mty.jls.rbac.api.ISysDeptService;
import com.mty.jls.rbac.api.ISysUserService;
import com.mty.jls.rbac.bean.IRoleDTO;
import com.mty.jls.utils.RbacUtil;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Classname ThisLevelChildenDataScope
 * @Description 本级以及子级
 * @Author Created by 蒋老湿
 * @Date 2019-06-08 16:30
 * @Version 1.0
 */
@Component("3")
public class ThisLevelChildenDataScope implements AbstractDataScopeHandler {

    @Reference(version = "1.0.0")
    private ISysUserService userService;

    @Reference(version = "1.0.0")
    private ISysDeptService deptService;


    @Override
    public List<Integer> getDeptIds(IRoleDTO roleDto, DataScopeTypeEnum dataScopeTypeEnum) {
        Integer deptId = userService.findByUserInfoName(RbacUtil.getSecurityUser().getUsername()).getDeptId();
        return deptService.selectDeptIds(deptId);
    }
}
