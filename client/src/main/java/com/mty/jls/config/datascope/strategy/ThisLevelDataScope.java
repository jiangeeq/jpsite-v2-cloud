package com.mty.jls.config.datascope.strategy;


import com.mty.jls.contract.enums.DataScopeTypeEnum;
import com.mty.jls.rbac.dto.RoleDTO;
import com.mty.jls.rbac.service.ISysUserService;
import com.mty.jls.utils.RbacUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @Classname ThisLevelHandler
 * @Description 本级
 * @Author Created by 蒋老湿
 * @Date 2019-06-08 15:44
 * @Version 1.0
 */
@Component("2")
public class ThisLevelDataScope implements AbstractDataScopeHandler {

    @Autowired
    private ISysUserService userService;

    @Override
    public List<Integer> getDeptIds(RoleDTO roleDto, DataScopeTypeEnum dataScopeTypeEnum) {
        // 用于存储部门id
        List<Integer> deptIds = new ArrayList<>();
        deptIds.add(userService.findByUserInfoName(RbacUtil.getSecurityUser().getUsername()).getDeptId());
        return deptIds;
    }
}
