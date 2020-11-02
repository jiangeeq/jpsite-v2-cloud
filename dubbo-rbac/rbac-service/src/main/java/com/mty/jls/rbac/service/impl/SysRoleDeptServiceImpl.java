package com.mty.jls.rbac.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dove.jls.common.utils.BeanPlusUtil;
import com.mty.jls.rbac.api.ISysRoleDeptService;
import com.mty.jls.rbac.bean.ISysRoleDept;
import com.mty.jls.rbac.domain.SysRoleDept;
import com.mty.jls.rbac.mapper.SysRoleDeptMapper;
import org.apache.dubbo.config.annotation.Service;


import java.util.List;

/**
 * <p>
 * 角色与部门对应关系 服务实现类
 * </p>
 */
@Service(cluster = "failsafe",
        loadbalance = "roundrobin",
        version = "1.0.0"
)
public class SysRoleDeptServiceImpl extends ServiceImpl<SysRoleDeptMapper, SysRoleDept> implements ISysRoleDeptService {


    @Override
    public List<ISysRoleDept> getRoleDeptIds(int roleId) {
        final List<SysRoleDept> sysRoleDepts =
                baseMapper.selectList(Wrappers.<SysRoleDept>lambdaQuery().select(SysRoleDept::getDeptId).eq(SysRoleDept::getRoleId, roleId));

        return BeanPlusUtil.copyListProperties(sysRoleDepts, ISysRoleDept::new);
    }
}
