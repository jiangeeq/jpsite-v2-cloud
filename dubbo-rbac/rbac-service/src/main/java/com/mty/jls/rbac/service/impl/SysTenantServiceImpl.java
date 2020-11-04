package com.mty.jls.rbac.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dove.jls.common.bean.PageRequest;
import com.dove.jls.common.utils.BeanPlusUtil;
import com.mty.jls.rbac.api.ISysTenantService;
import com.mty.jls.rbac.api.ISysUserRoleService;
import com.mty.jls.rbac.bean.IPageResponse;
import com.mty.jls.rbac.bean.ISysTenant;
import com.mty.jls.rbac.constant.MenuConstant;
import com.mty.jls.rbac.domain.SysDept;
import com.mty.jls.rbac.domain.SysMenu;
import com.mty.jls.rbac.domain.SysRole;
import com.mty.jls.rbac.domain.SysRoleMenu;
import com.mty.jls.rbac.domain.SysTenant;
import com.mty.jls.rbac.domain.SysUser;
import com.mty.jls.rbac.domain.SysUserRole;
import com.mty.jls.rbac.mapper.SysDeptMapper;
import com.mty.jls.rbac.mapper.SysMenuMapper;
import com.mty.jls.rbac.mapper.SysRoleMapper;
import com.mty.jls.rbac.mapper.SysRoleMenuMapper;
import com.mty.jls.rbac.mapper.SysTenantMapper;
import com.mty.jls.rbac.mapper.SysUserMapper;
import com.mty.jls.rbac.mapper.SysUserRoleMapper;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 租户表 服务实现类
 * </p>
 *
 * @author 蒋老湿
 * @since 2019-08-10
 */
@Service(cluster = "failsafe",
        loadbalance = "roundrobin",
        version = "1.0.0"
)
public class SysTenantServiceImpl extends ServiceImpl<SysTenantMapper, SysTenant> implements ISysTenantService {
    private final Set<String> noCommonMenus = Set.of("代码生成", "租户管理");

    @Autowired
    private SysTenantMapper sysTenantMapper;
    @Autowired
    private SysRoleMenuMapper roleMenuMapper;
    @Autowired
    private SysDeptMapper deptMapper;
    @Autowired
    private SysMenuMapper menuMapper;
    @Autowired
    private SysUserMapper userMapper;
    @Autowired
    private SysRoleMapper roleMapper;
    @Autowired
    private SysUserRoleMapper userRoleMapper;


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
    public boolean saveTenant(ISysTenant sysTenant) {
        SysTenant tenant = BeanPlusUtil.copySingleProperties(sysTenant, SysTenant::new);
        this.save(tenant);
        initTenantRelatedData(tenant.getId());
        return true;
    }

    /**
     * 初始化有关系的租户边缘数据
     */
    private void initTenantRelatedData(int tenantId) {
        // 插入部门
        SysDept dept = new SysDept();
        dept.setName("默认部门");
        dept.setParentId(0);
        dept.setSort(0);
        dept.setTenantId(tenantId);
        deptMapper.insert(dept);
        // 构造初始化用户
        SysUser user = new SysUser();
        user.setUsername("root");
        // 默认密码 123456
        user.setPassword("$2a$10$SRc6rn950fpwm.qTQIaQt.UQQtXh8b.i2/.qpaAppPKb2S/wWSZWW");
        user.setDeptId(dept.getDeptId());
        user.setTenantId(tenantId);
        userMapper.insert(user);
        // 构造新角色
        SysRole role = new SysRole();
        role.setRoleCode("ROLE_ADMIN");
        role.setRoleName("默认角色");
        role.setDsType(1);
        // 默认全部权限
        var deptIds = deptMapper.selectList(Wrappers.lambdaQuery()).stream().map(SysDept::getDeptId).collect(Collectors.toList());
        role.setDsScope(CollectionUtil.join(deptIds, ","));
        role.setTenantId(tenantId);
        roleMapper.insert(role);
        // 用户角色关系
        SysUserRole userRole = new SysUserRole();
        userRole.setUserId(user.getUserId());
        userRole.setRoleId(role.getRoleId());
        userRole.setTenantId(tenantId);
        userRoleMapper.insert(userRole);
        // 菜单角色关系
        List<SysMenu> autoMenuList = menuMapper.selectList(Wrappers.lambdaQuery());
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
        roleMenuMapper.saveBatch(collect);
    }

    @Override
    public List<ISysTenant> getNormalTenant() {
        List<SysTenant> sysTenants = list(Wrappers.<SysTenant>lambdaQuery()
                // 状态为0的
                .eq(SysTenant::getStatus, 0)
                // 开始时间小于等于现在的时间
                .le(SysTenant::getStartTime, LocalDateTime.now())
                // 结束时间大于等于现在的时间
                .ge(SysTenant::getEndTime, LocalDateTime.now()));
        return BeanPlusUtil.copyListProperties(sysTenants, ISysTenant::new);
    }

    @Override
    public Boolean updateById(ISysTenant sysTenant) {
        return baseMapper.updateById(BeanPlusUtil.copySingleProperties(sysTenant, SysTenant::new)) > 0;
    }

    @Override
    public Boolean removeById(Integer id) {
        return baseMapper.deleteById(id) > 0;
    }

    @Override
    public IPageResponse<List<ISysTenant>> page(PageRequest pageRequest, ISysTenant sysTenant) {
        Page<SysTenant> page = new Page<>(pageRequest.getPageNumber(), pageRequest.getPageSize());
        Page<SysTenant> tenantPage = baseMapper.selectPage(page, Wrappers.query(BeanPlusUtil.copySingleProperties(sysTenant, SysTenant::new)));

        IPageResponse<List<ISysTenant>> iPageResponse = IPageResponse.ok();
        iPageResponse.setPage(tenantPage.getCurrent()).setPageSize(tenantPage.getSize()).setTotal(tenantPage.getTotal())
                .setRecords(BeanPlusUtil.copyListProperties(tenantPage.getRecords(), ISysTenant::new));
        return iPageResponse;
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
