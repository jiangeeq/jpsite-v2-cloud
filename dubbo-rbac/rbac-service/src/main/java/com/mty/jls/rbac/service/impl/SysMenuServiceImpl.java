package com.mty.jls.rbac.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mty.jls.rbac.api.ISysMenuService;
import com.mty.jls.rbac.bean.ISysMenu;
import com.mty.jls.rbac.mapper.SysMenuMapper;
import com.mty.jls.rbac.bean.IMenuDTO;
import com.mty.jls.rbac.constant.MenuConstant;
import com.mty.jls.rbac.domain.SysMenu;
import com.mty.jls.rbac.api.ISysRoleMenuService;
import com.mty.jls.rbac.util.RbacUtil;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service(cluster = "failsafe",
        loadbalance = "roundrobin",
        version = "1.0.0"
)
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements ISysMenuService {

    @Autowired
    private ISysRoleMenuService roleMenuService;

//    @Override
//    public boolean save(ISysMenu sysMenu) {
//        SysMenu menu = new SysMenu();
//        BeanUtil.copyProperties(sysMenu, menu);
//        // 菜单校验
//        verifyForm(menu);
//        return super.save(menu);
//    }

    @Override
    public boolean updateMenuById(IMenuDTO entity) {
        SysMenu sysMenu = new SysMenu();
        BeanUtils.copyProperties(entity, sysMenu);
        // 菜单校验
        verifyForm(sysMenu);
        return this.updateById(sysMenu);
    }

    @Override
    public List<ISysMenu> selectMenuTree(Integer uid) {

        LambdaQueryWrapper<SysMenu> sysMenuLambdaQueryWrapper = Wrappers.<SysMenu>query().lambda();
        sysMenuLambdaQueryWrapper.select(SysMenu::getMenuId, SysMenu::getName, SysMenu::getPerms, SysMenu::getPath, SysMenu::getParentId,
                SysMenu::getComponent, SysMenu::getIsFrame, SysMenu::getIcon, SysMenu::getSort, SysMenu::getType, SysMenu::getDelFlag);
        // 所有人有权限看到 只是没有权限操作而已 暂定这样
        if (uid != 0) {
            List<Integer> menuIdList = roleMenuService.getMenuIdByUserId(uid);
            sysMenuLambdaQueryWrapper.in(SysMenu::getMenuId, menuIdList);
        }
        List<SysMenu> parentMenus = new ArrayList<>();
        List<SysMenu> menus = baseMapper.selectList(sysMenuLambdaQueryWrapper);
        menus.forEach(menu -> {
            if (menu.getParentId() == null || menu.getParentId() == 0) {
                menu.setLevel(0);
                if (RbacUtil.exists(parentMenus, menu)) {
                    parentMenus.add(menu);
                }
            }
        });
        RbacUtil.findChildren(parentMenus, menus, 0);
        parentMenus.sort(Comparator.comparing(SysMenu::getSort));

        ArrayList<ISysMenu> iSysMenus = new ArrayList<>();
        BeanUtil.copyProperties(parentMenus, iSysMenus);
        return iSysMenus;
    }

    @Override
    public ISysMenu getMenuById(Integer parentId) {
        SysMenu sysMenu = baseMapper.selectOne(Wrappers.<SysMenu>lambdaQuery().select(SysMenu::getType).eq(SysMenu::getMenuId, parentId));

        ISysMenu iSysMenu = new ISysMenu();
        BeanUtil.copyProperties(sysMenu, iSysMenu);
        return iSysMenu;
    }

    @Override
    public List<String> findPermsByUserId(Integer userId) {
        return baseMapper.findPermsByUserId(userId);
    }

    @Override
    public boolean removeMenuById(Serializable id) {
        List<Integer> idList =
                this.list(Wrappers.<SysMenu>query().lambda().eq(SysMenu::getParentId, id)).stream().map(SysMenu::getMenuId).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(idList)) {
            return false;
        }
        return this.removeById(id);
    }

    /**
     * 验证菜单参数是否正确
     */
    private void verifyForm(SysMenu menu) {
        //上级菜单类型
        int parentType = MenuConstant.MenuType.CATALOG.getValue();
        if (menu.getParentId() != 0) {
            SysMenu parentMenu = baseMapper.selectOne(Wrappers.<SysMenu>lambdaQuery().select(SysMenu::getType).eq(SysMenu::getMenuId, menu.getParentId()));
            parentType = parentMenu.getType();
        }
        //目录、菜单
        if (menu.getType() == MenuConstant.MenuType.CATALOG.getValue() ||
                menu.getType() == MenuConstant.MenuType.MENU.getValue()) {
            if (parentType != MenuConstant.MenuType.CATALOG.getValue()) {
//                throw new BaseException("上级菜单只能为目录类型");
            }
            return;
        }
        //按钮
        if (menu.getType() == MenuConstant.MenuType.BUTTON.getValue()) {
            if (parentType != MenuConstant.MenuType.MENU.getValue()) {
//                throw new BaseException("上级菜单只能为菜单类型");
            }
        }
    }
}
