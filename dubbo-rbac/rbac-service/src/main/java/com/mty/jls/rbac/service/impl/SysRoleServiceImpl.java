package com.mty.jls.rbac.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dove.jls.common.utils.BeanPlusUtil;
import com.mty.jls.rbac.api.ISysRoleDeptService;
import com.mty.jls.rbac.api.ISysRoleService;
import com.mty.jls.rbac.bean.IRoleDTO;
import com.mty.jls.rbac.bean.ISysMenu;
import com.mty.jls.rbac.bean.ISysRole;
import com.mty.jls.rbac.bean.ISysRoleDept;
import com.mty.jls.rbac.bean.ISysRoleMenu;
import com.mty.jls.rbac.domain.SysMenu;
import com.mty.jls.rbac.domain.SysRole;
import com.mty.jls.rbac.domain.SysRoleDept;
import com.mty.jls.rbac.domain.SysRoleMenu;
import com.mty.jls.rbac.mapper.SysRoleDeptMapper;
import com.mty.jls.rbac.mapper.SysRoleMapper;
import com.mty.jls.rbac.mapper.SysRoleMenuMapper;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Service(cluster = "failsafe",
        loadbalance = "roundrobin",
        group = "rbac",
        version = "1.0.0"
)
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {

    @Resource
    private SysRoleMenuMapper roleMenuMapper;

    @Resource
    private SysRoleDeptMapper roleDeptMapper;

    @Resource
    private ISysRoleDeptService roleDeptService;


    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveRoleMenu(IRoleDTO roleDto) {
        SysRole sysRole = BeanPlusUtil.copySingleProperties(roleDto, SysRole::new);
        // 根据数据权限范围查询部门ids
        List<Integer> deptIds = CollectionUtil.newArrayList();

        sysRole.setDsScope("");

        baseMapper.insertRole(sysRole);

        Integer roleId = sysRole.getRoleId();
        //维护角色菜单
        List<ISysRoleMenu> roleMenus = roleDto.getRoleMenus();
        if (CollectionUtil.isNotEmpty(roleMenus)) {
            saveRoleMenus(roleId, roleMenus);
        }
        // 维护角色部门权限
        // 根据数据权限范围查询部门ids
        if (CollectionUtil.isNotEmpty(deptIds)) {
            saveRoleDept(roleId, deptIds);
        }
        return true;
    }

    private void saveRoleDept(Integer roleId, List<Integer> deptIds) {
        List<SysRoleDept> roleDepts = deptIds.stream().map(integer -> {
            SysRoleDept sysRoleDept = new SysRoleDept();
            sysRoleDept.setDeptId(integer);
            sysRoleDept.setRoleId(roleId);
            return sysRoleDept;
        }).collect(Collectors.toList());

        roleDeptMapper.saveBatch(roleDepts);
    }

    private void saveRoleMenus(Integer roleId, List<ISysRoleMenu> roleMenus) {
        List<SysRoleMenu> rms = roleMenus.stream().map(sysRoleMenu -> {
            SysRoleMenu roleMenu = new SysRoleMenu();
            roleMenu.setRoleId(roleId);
            roleMenu.setMenuId(sysRoleMenu.getMenuId());
            return roleMenu;
        }).collect(Collectors.toList());


        roleMenuMapper.saveBatch(rms);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateRoleMenu(IRoleDTO roleDto) {
        SysRole sysRole = BeanPlusUtil.copySingleProperties(roleDto, SysRole::new);

        List<ISysRoleMenu> roleMenus = roleDto.getRoleMenus();
        roleMenuMapper.delete(Wrappers.<SysRoleMenu>query().lambda().eq(SysRoleMenu::getRoleId, sysRole.getRoleId()));
        roleDeptMapper.delete(Wrappers.<SysRoleDept>query().lambda().eq(SysRoleDept::getRoleId, sysRole.getRoleId()));

        if (CollectionUtil.isNotEmpty(roleMenus)) {
            final List<SysRoleMenu> sysRoleMenus = BeanPlusUtil.copyListProperties(roleMenus, SysRoleMenu::new);
            roleMenuMapper.saveBatch(sysRoleMenus);
        }
        // 根据数据权限范围查询部门ids
        List<Integer> deptIds = CollectionUtil.newArrayList();

        sysRole.setDsScope("");
        baseMapper.updateById(sysRole);
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean removeById(Serializable id) {
        roleMenuMapper.delete(Wrappers.<SysRoleMenu>query().lambda().eq(SysRoleMenu::getRoleId, id));
        roleDeptMapper.delete(Wrappers.<SysRoleDept>query().lambda().eq(SysRoleDept::getRoleId, id));
        return super.removeById(id);
    }

    @Override
    public List<ISysRole> selectRoleList(String roleName) {
        LambdaQueryWrapper<SysRole> sysRoleLambdaQueryWrapper = Wrappers.<SysRole>lambdaQuery();
        if (StrUtil.isNotEmpty(roleName)) {
            sysRoleLambdaQueryWrapper.like(SysRole::getRoleName, roleName);
        }
        List<SysRole> sysRoles = baseMapper.selectList(sysRoleLambdaQueryWrapper);

        final List<SysRole> sysRoleList = sysRoles.stream().peek(sysRole ->
                sysRole.setRoleDeptIds(roleDeptService.getRoleDeptIds(sysRole.getRoleId()).stream().map(ISysRoleDept::getDeptId).collect(Collectors.toList()))
        ).collect(Collectors.toList());

        final List<ISysRole> iSysRoles = BeanPlusUtil.copyListProperties(sysRoleList, ISysRole::new);
        return iSysRoles;
    }


    @Override
    public List<ISysMenu> findMenuListByRoleId(int roleId) {
        final List<SysMenu> menuListByRoleId = baseMapper.findMenuListByRoleId(roleId);
        final List<ISysMenu> iSysMenus = BeanPlusUtil.copyListProperties(menuListByRoleId, ISysMenu::new);
        return iSysMenus;
    }

    @Override
    public List<ISysRole> findRolesByUserId(Integer userId) {
        final List<SysRole> sysRoles = baseMapper.listRolesByUserId(userId);
        final List<ISysRole> iSysRoles = BeanPlusUtil.copyListProperties(sysRoles, ISysRole::new);
        return iSysRoles;
    }

}
