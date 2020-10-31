package com.mty.jls.rbac.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.mty.jls.rbac.domain.SysRoleMenu;
import com.mty.jls.rbac.mapper.SysRoleMenuMapper;
import com.mty.jls.rbac.service.ISysRoleMenuService;
import org.springframework.stereotype.Service;

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
