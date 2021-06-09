package com.mty.jls.rbac.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.mty.jls.rbac.mapper.SysRoleMenuMapper;
import com.mty.jls.rbac.domain.SysRoleMenu;
import com.mty.jls.rbac.api.ISysRoleMenuService;
import org.apache.dubbo.config.annotation.Service;

import java.util.List;

@Service(cluster = "failsafe",
        loadbalance = "roundrobin",
        group = "rbac",
        version = "1.0.0"
)
public class SysRoleMenuServiceImpl extends ServiceImpl<SysRoleMenuMapper, SysRoleMenu> implements ISysRoleMenuService {

    @Override
    public List<Integer> getMenuIdByUserId(Integer userId) {
        return baseMapper.getMenuIdByUserId(userId);
    }
}
