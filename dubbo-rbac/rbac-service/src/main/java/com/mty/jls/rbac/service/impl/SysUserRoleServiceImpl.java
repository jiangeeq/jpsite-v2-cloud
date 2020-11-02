package com.mty.jls.rbac.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dove.jls.common.utils.BeanPlusUtil;
import com.mty.jls.rbac.api.ISysUserRoleService;
import com.mty.jls.rbac.bean.ISysUserRole;
import com.mty.jls.rbac.domain.SysUserRole;
import com.mty.jls.rbac.mapper.SysUserRoleMapper;
import org.apache.dubbo.config.annotation.Service;


import java.util.List;

/**
 * <p>
 * 用户角色表 服务实现类
 * </p>
 *
 * @author 蒋老湿
 * @since 2019-04-21
 */
@Service(cluster = "failsafe",
        loadbalance = "roundrobin",
        version = "1.0.0"
)
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole> implements ISysUserRoleService {

    @Override
    public boolean save(ISysUserRole entity) {
        final SysUserRole sysUserRole = BeanPlusUtil.copySingleProperties(entity, SysUserRole::new);
        return baseMapper.insert(sysUserRole) > 0;
    }


    @Override
    public List<ISysUserRole> selectUserRoleListByUserId(Integer userId) {
        final List<SysUserRole> sysUserRoles = baseMapper.selectUserRoleListByUserId(userId);
        final List<ISysUserRole> iSysUserRoles = BeanPlusUtil.copyListProperties(sysUserRoles, ISysUserRole::new);
        return iSysUserRoles;
    }
}
