package com.mty.jls.config.datascope.strategy;


import com.mty.jls.contract.enums.DataScopeTypeEnum;
import com.mty.jls.rbac.dto.RoleDTO;
import com.mty.jls.rbac.service.ISysDeptService;
import com.mty.jls.rbac.service.ISysUserService;
import com.mty.jls.utils.RbacUtil;
import org.apache.catalina.security.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISysDeptService deptService;


    @Override
    public List<Integer> getDeptIds(RoleDTO roleDto, DataScopeTypeEnum dataScopeTypeEnum) {
        Integer deptId = userService.findByUserInfoName(RbacUtil.getSecurityUser().getUsername()).getDeptId();
        return deptService.selectDeptIds(deptId);
    }
}
