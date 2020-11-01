//package com.mty.jls.rbac.service.impl;
//
//import cn.hutool.core.collection.CollectionUtil;
//import cn.hutool.core.util.StrUtil;
//import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
//import com.baomidou.mybatisplus.core.toolkit.Wrappers;
//import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
//
//
//import com.mty.jls.config.datascope.strategy.DataScopeContext;
//import com.mty.jls.dovecommon.utils.BeanPlusUtil;
//import com.mty.jls.rbac.domain.SysMenu;
//import com.mty.jls.rbac.domain.SysRole;
//import com.mty.jls.rbac.domain.SysRoleDept;
//import com.mty.jls.rbac.domain.SysRoleMenu;
//import com.mty.jls.rbac.dto.RoleDTO;
//import com.mty.jls.rbac.mapper.SysRoleMapper;
//import com.mty.rbac.api.ISysRoleDeptService;
//import com.mty.rbac.api.ISysRoleMenuService;
//import com.mty.rbac.api.ISysRoleService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import javax.annotation.Resource;
//import java.io.Serializable;
//import java.util.List;
//import java.util.StringJoiner;
//import java.util.stream.Collectors;
//
//@Service
//public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {
//
//    @Resource
//    private ISysRoleMenuService roleMenuService;
//
//    @Resource
//    private ISysRoleDeptService roleDeptService;
//
//    @Autowired
//    private DataScopeContext dataScopeContext;
//
//    @Transactional(rollbackFor = Exception.class)
//    @Override
//    public boolean saveRoleMenu(RoleDTO roleDto) {
//        SysRole sysRole = BeanPlusUtil.copySingleProperties(roleDto, SysRole::new);
//        // 根据数据权限范围查询部门ids
//        List<Integer> deptIds = dataScopeContext.getDeptIdsForDataScope(roleDto, roleDto.getDsType());
//
//        StringJoiner dsScope = new StringJoiner(",");
//        deptIds.forEach(deptId -> dsScope.add(Integer.toString(deptId)));
//        sysRole.setDsScope(dsScope.toString());
//
//        baseMapper.insertRole(sysRole);
//
//        Integer roleId = sysRole.getRoleId();
//        //维护角色菜单
//        List<SysRoleMenu> roleMenus = roleDto.getRoleMenus();
//        if (CollectionUtil.isNotEmpty(roleMenus)) {
//            saveRoleMenus(roleId, roleMenus);
//        }
//        // 维护角色部门权限
//        // 根据数据权限范围查询部门ids
//        if (CollectionUtil.isNotEmpty(deptIds)) {
//            saveRoleDept(roleId, deptIds);
//        }
//        return true;
//    }
//
//    private void saveRoleDept(Integer roleId, List<Integer> deptIds) {
//        List<SysRoleDept> roleDepts = deptIds.stream().map(integer -> {
//            SysRoleDept sysRoleDept = new SysRoleDept();
//            sysRoleDept.setDeptId(integer);
//            sysRoleDept.setRoleId(roleId);
//            return sysRoleDept;
//        }).collect(Collectors.toList());
//
//        roleDeptService.saveBatch(roleDepts);
//    }
//
//    private void saveRoleMenus(Integer roleId, List<SysRoleMenu> roleMenus) {
//        List<SysRoleMenu> rms = roleMenus.stream().map(sysRoleMenu -> {
//            SysRoleMenu roleMenu = new SysRoleMenu();
//            roleMenu.setRoleId(roleId);
//            roleMenu.setMenuId(sysRoleMenu.getMenuId());
//            return roleMenu;
//        }).collect(Collectors.toList());
//        roleMenuService.saveBatch(rms);
//    }
//
//    @Transactional(rollbackFor = Exception.class)
//    @Override
//    public boolean updateRoleMenu(RoleDTO roleDto) {
//        SysRole sysRole = BeanPlusUtil.copySingleProperties(roleDto, SysRole::new);
//
//        List<SysRoleMenu> roleMenus = roleDto.getRoleMenus();
//        roleMenuService.remove(Wrappers.<SysRoleMenu>query().lambda().eq(SysRoleMenu::getRoleId, sysRole.getRoleId()));
//        roleDeptService.remove(Wrappers.<SysRoleDept>query().lambda().eq(SysRoleDept::getRoleId, sysRole.getRoleId()));
//
//        if (CollectionUtil.isNotEmpty(roleMenus)) {
//            roleMenuService.saveBatch(roleMenus);
//        }
//        // 根据数据权限范围查询部门ids
//        List<Integer> deptIds = dataScopeContext.getDeptIdsForDataScope(roleDto, roleDto.getDsType());
//
//        StringJoiner dsScope = new StringJoiner(",");
//        deptIds.forEach(integer -> {
//            dsScope.add(Integer.toString(integer));
//        });
//        if (CollectionUtil.isNotEmpty(deptIds)) {
//            saveRoleDept(roleDto.getRoleId(), deptIds);
//        }
//        sysRole.setDsScope(dsScope.toString());
//        baseMapper.updateById(sysRole);
//        return true;
//    }
//
//    @Transactional(rollbackFor = Exception.class)
//    @Override
//    public boolean removeById(Serializable id) {
//        roleMenuService.remove(Wrappers.<SysRoleMenu>query().lambda().eq(SysRoleMenu::getRoleId, id));
//        roleDeptService.remove(Wrappers.<SysRoleDept>query().lambda().eq(SysRoleDept::getRoleId, id));
//        return super.removeById(id);
//    }
//
//    @Override
//    public List<SysRole> selectRoleList(String roleName) {
//        LambdaQueryWrapper<SysRole> sysRoleLambdaQueryWrapper = Wrappers.<SysRole>lambdaQuery();
//        if (StrUtil.isNotEmpty(roleName)) {
//            sysRoleLambdaQueryWrapper.like(SysRole::getRoleName, roleName);
//        }
//        List<SysRole> sysRoles = baseMapper.selectList(sysRoleLambdaQueryWrapper);
//        return sysRoles.stream().peek(sysRole ->
//                sysRole.setRoleDeptIds(roleDeptService.getRoleDeptIds(sysRole.getRoleId()).stream().map(SysRoleDept::getDeptId).collect(Collectors.toList()))
//        ).collect(Collectors.toList());
//    }
//
//
//    @Override
//    public List<SysMenu> findMenuListByRoleId(int roleId) {
//        return baseMapper.findMenuListByRoleId(roleId);
//    }
//
//    @Override
//    public List<SysRole> findRolesByUserId(Integer userId) {
//        return baseMapper.listRolesByUserId(userId);
//    }
//
//}
