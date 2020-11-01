package com.mty.rbac.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.mty.rbac.domain.SysRoleMenu;
import com.mty.rbac.mapper.SysRoleMenuMapper;
import com.mty.rbac.api.ISysRoleMenuService;
import org.apache.dubbo.config.annotation.Service;

import java.util.List;

/**
 * <p>
 * 角色菜单表 服务实现类
 * </p>

 */
@Service
public class SysRoleMenuServiceImpl extends ServiceImpl<SysRoleMenuMapper, SysRoleMenu> implements ISysRoleMenuService {

    @Override
    public List<Integer> getMenuIdByUserId(Integer userId) {
        return baseMapper.getMenuIdByUserId(userId);
    }
}
