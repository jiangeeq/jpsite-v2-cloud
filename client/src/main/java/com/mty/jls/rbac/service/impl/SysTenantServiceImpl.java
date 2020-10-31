package com.mty.jls.rbac.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mty.jls.config.datascope.strategy.DataScopeContext;
import com.mty.jls.config.mybatisplus.TenantContextHolder;
import com.mty.jls.contract.constant.MenuConstant;
import com.mty.jls.rbac.mapper.SysTenantMapper;
import com.mty.jls.rbac.service.ISysDeptService;
import com.mty.jls.rbac.service.ISysMenuService;
import com.mty.jls.rbac.service.ISysRoleMenuService;
import com.mty.jls.rbac.service.ISysRoleService;
import com.mty.jls.rbac.service.ISysTenantService;
import com.mty.jls.rbac.service.ISysUserRoleService;
import com.mty.jls.rbac.service.ISysUserService;
import com.mty.jls.utils.RbacUtil;

import com.mty.jls.rbac.domain.SysDept;
import com.mty.jls.rbac.domain.SysMenu;
import com.mty.jls.rbac.domain.SysRole;
import com.mty.jls.rbac.domain.SysRoleMenu;
import com.mty.jls.rbac.domain.SysTenant;
import com.mty.jls.rbac.domain.SysUser;
import com.mty.jls.rbac.domain.SysUserRole;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.StringJoiner;
import java.util.stream.Collectors;

/**
 * <p>
 * 租户表 服务实现类
 * </p>
 *
 * @author 蒋老湿
 * @since 2019-08-10
 */
@Service
public class SysTenantServiceImpl extends ServiceImpl<SysTenantMapper, SysTenant> implements ISysTenantService {
    private final Set<String> noCommonMenus = Set.of("代码生成", "租户管理");

    @Autowired
    private ISysUserRoleService userRoleService;
    @Autowired
    private ISysDeptService deptService;
    @Autowired
    private ISysMenuService menuService;
    @Autowired
    private ISysUserService userService;
    @Autowired
    private ISysRoleService roleService;
    @Autowired
    private ISysRoleMenuService roleMenuService;
    @Autowired
    private DataScopeContext dataScopeContext;


    /**
     * 一般租户授权时
     * 1.保存租户
     * 2.初始化权限相关表
     *
     * @param sysTenant
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveTenant(SysTenant sysTenant) {
        this.save(sysTenant);
        // 修改租户Id，这样每次初始化的相关数据都是在新租户下
        TenantContextHolder.setCurrentTenantId(Long.valueOf(sysTenant.getId()));
        initTenantRelatedData();
        return true;
    }

    /**
     * 初始化有关系的租户边缘数据
     */
    private void initTenantRelatedData() {
        // 插入部门
        SysDept dept = new SysDept();
        dept.setName("默认部门");
        dept.setParentId(0);
        dept.setSort(0);
        deptService.save(dept);
        // 构造初始化用户
        SysUser user = new SysUser();
        user.setUsername("root");
        // 默认密码
        user.setPassword(RbacUtil.encode("123456"));
        user.setDeptId(dept.getDeptId());
        userService.save(user);
        // 构造新角色
        SysRole role = new SysRole();
        role.setRoleCode("ROLE_ADMIN");
        role.setRoleName("默认角色");
        role.setDsType(1);
        // 默认全部权限
        List<Integer> deptIds = dataScopeContext.getDeptIdsForDataScope(null, 1);
        StringJoiner dsScope = new StringJoiner(",");
        deptIds.forEach(integer -> dsScope.add(Integer.toString(integer)));
        role.setDsScope(dsScope.toString());
        roleService.save(role);
        // 用户角色关系
        SysUserRole userRole = new SysUserRole();
        userRole.setUserId(user.getUserId());
        userRole.setRoleId(role.getRoleId());
        userRoleService.save(userRole);
        // 菜单角色关系
        List<SysMenu> autoMenuList = menuService.list();
        List<SysMenu> sysMenuList = new ArrayList<>();
        autoMenuList.forEach(sysMenu -> {
            if (sysMenu.getType() == MenuConstant.MenuType.MENU.getValue()) {
                if (noCommonMenus.contains(sysMenu.getName())) {
                    List<SysMenu> treeMenuList = treeMenuList(autoMenuList, sysMenu.getMenuId());
                    treeMenuList.add(sysMenu);
                    sysMenuList.addAll(treeMenuList);
                }
            }
        });
        sysMenuList.forEach(sysMenu -> {
            autoMenuList.removeIf(next -> next.getMenuId().equals(sysMenu.getMenuId()));
        });
        // 查询全部菜单,构造角色菜单关系
        List<SysRoleMenu> collect = autoMenuList
                .stream().map(menu -> {
                    SysRoleMenu roleMenu = new SysRoleMenu();
                    roleMenu.setRoleId(role.getRoleId());
                    roleMenu.setMenuId(menu.getMenuId());
                    return roleMenu;
                }).collect(Collectors.toList());
        roleMenuService.saveBatch(collect);
    }

    @Override
    public List<SysTenant> getNormalTenant() {
        return list(Wrappers.<SysTenant>lambdaQuery()
                // 状态为0的
                .eq(SysTenant::getStatus, 0)
                // 开始时间小于等于现在的时间
                .le(SysTenant::getStartTime, LocalDateTime.now())
                // 结束时间大于等于现在的时间
                .ge(SysTenant::getEndTime, LocalDateTime.now()));
    }

    /**
     * 找出集合中menuId的子菜单
     *
     * @param menuList 所有菜单集合
     * @param menuId   父菜单
     * @return 父菜单的子菜单集合
     */
    private List<SysMenu> treeMenuList(List<SysMenu> menuList, int menuId) {
        List<SysMenu> childMenu = new ArrayList<>();
        for (SysMenu mu : menuList) {
            //遍历出父id等于参数的id，add进子节点集合
            if (mu.getParentId() == menuId) {
                //递归遍历下一级
                treeMenuList(menuList, mu.getMenuId());
                childMenu.add(mu);
            }
        }
        return childMenu;
    }
}
