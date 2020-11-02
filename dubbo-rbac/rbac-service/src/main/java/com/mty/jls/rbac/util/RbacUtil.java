package com.mty.jls.rbac.util;

import com.mty.jls.rbac.domain.SysDept;
import com.mty.jls.rbac.vo.DeptTreeVo;
import com.mty.jls.rbac.vo.MenuMetaVo;
import com.mty.jls.rbac.vo.MenuVo;
import com.mty.jls.rbac.domain.SysMenu;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * @Classname RbacUtil
 * @Description pre系统用户工具类
 * @Author 蒋老湿 蒋老湿
 * @Date 2019-04-27 16:52
 * @Version 1.0
 */
@UtilityClass
@Slf4j
public class RbacUtil {


    public List<MenuVo> buildMenus(List<SysMenu> sysMenuList) {
        List<MenuVo> list = new LinkedList<>();

        sysMenuList.forEach(sysMenu -> {
                    if (sysMenu != null) {
                        List<SysMenu> menuChildren = sysMenu.getChildren();
                        MenuVo menuVo = new MenuVo();
                        menuVo.setName(sysMenu.getName());
                        menuVo.setPath(sysMenu.getPath());
                        menuVo.setMeta(new MenuMetaVo(sysMenu.getName(), sysMenu.getIcon()));
                        // 如果不是外链
                        if (sysMenu.getIsFrame()) {
                            if (sysMenu.getParentId().equals(0)) {
                                //一级目录需要加斜杠，不然访问 会跳转404页面
                                menuVo.setPath("/" + sysMenu.getPath());
                                menuVo.setComponent(StringUtils.isEmpty(sysMenu.getComponent()) ? "Layout" : sysMenu.getComponent());
                            } else if (Strings.isNotEmpty(sysMenu.getComponent())) {
                                menuVo.setComponent(sysMenu.getComponent());
                            }
                        }
                        var isDirectory = sysMenu.getType() == 0;
                        var hasMenuChildren = !CollectionUtils.isEmpty(menuChildren);
                        if (isDirectory && hasMenuChildren) {
                            menuVo.setChildren(buildMenus(menuChildren));
                        } else if (sysMenu.getParentId().equals(0)) {
                            // 处理是一级菜单并且没有子菜单的情况
                            menuVo.setAlwaysShow(false);
                            menuVo.setRedirect("noredirect");
                            menuVo.setName(null);
                            menuVo.setMeta(null);
                            menuVo.setComponent("Layout");

                            MenuVo thisMenuVo = new MenuVo();
                            thisMenuVo.setMeta(menuVo.getMeta());
                            // 非外链
                            if (sysMenu.getIsFrame()) {
                                thisMenuVo.setPath("index");
                                thisMenuVo.setName(menuVo.getName());
                                thisMenuVo.setComponent(menuVo.getComponent());
                            } else {
                                thisMenuVo.setPath(sysMenu.getPath());
                            }

                            List<MenuVo> childMenuVoList = new ArrayList<>();
                            childMenuVoList.add(thisMenuVo);
                            menuVo.setChildren(childMenuVoList);
                        }
                        list.add(menuVo);
                    }
                }
        );
        return list;
    }


    /**
     * 遍历菜单
     *
     * @param parentMenuList
     * @param menus
     * @param menuType
     */
    public void findChildren(List<SysMenu> parentMenuList, List<SysMenu> menus, int menuType) {
        for (SysMenu parentMenu : parentMenuList) {
            List<SysMenu> children = new ArrayList<>();
            for (SysMenu menu : menus) {
                if (menuType == 1 && menu.getType() == 2) {
                    // 如果是获取类型不需要按钮，且菜单类型是按钮的，直接过滤掉
                    continue;
                }
                if (parentMenu.getMenuId() != null && parentMenu.getMenuId().equals(menu.getParentId())) {
                    menu.setParentName(parentMenu.getName());
                    menu.setLevel(parentMenu.getLevel() + 1);
                    if (exists(children, menu)) {
                        children.add(menu);
                    }
                }
            }
            children.sort(Comparator.comparing(SysMenu::getSort));
            parentMenu.setChildren(children);
            findChildren(children, menus, menuType);
        }
    }

    /**
     * 构建部门tree
     *
     * @param parentDeptList
     * @param allDeptList
     */
    public void findChildrenToDo(List<SysDept> parentDeptList, List<SysDept> allDeptList) {

        for (SysDept parentDept : parentDeptList) {
            DeptTreeVo parentDeptTreeVo = new DeptTreeVo().setId(parentDept.getDeptId()).setLabel(parentDept.getName());
            List<SysDept> childrenDeptList = new ArrayList<>();
            List<DeptTreeVo> childrenDeptVoList = new ArrayList<>();
            for (SysDept dept : allDeptList) {
                // 如果当前部门的ParentId 是 parentDept的id
                if (parentDept.getDeptId() != null && parentDept.getDeptId().equals(dept.getParentId())) {
                    dept.setParentName(parentDept.getName());
                    // 设置等级为parentDept的下一级
                    dept.setLevel(parentDept.getLevel() + 1);
                    DeptTreeVo deptTreeVo = new DeptTreeVo().setLabel(dept.getName()).setId(dept.getDeptId());
                    childrenDeptList.add(dept);
                    childrenDeptVoList.add(deptTreeVo);
                }
            }
            parentDept.setChildren(childrenDeptList);
            parentDeptTreeVo.setChildren(childrenDeptVoList);
            // 循环递归
            findChildrenToDo(childrenDeptList, allDeptList);
        }
    }

    /**
     * 构建部门tree
     *
     * @param parentDeptList
     * @param allDeptList
     */
    public void findChildrenToVo(List<DeptTreeVo> parentDeptList, List<SysDept> allDeptList) {

        for (DeptTreeVo parentDeptVo : parentDeptList) {
            List<DeptTreeVo> childrenDeptVoList = new ArrayList<>();
            for (SysDept dept : allDeptList) {
                if (parentDeptVo.getId() == dept.getParentId()) {
                    DeptTreeVo deptTreeVo = new DeptTreeVo();
                    deptTreeVo.setLabel(dept.getName());
                    deptTreeVo.setId(dept.getDeptId());
                    childrenDeptVoList.add(deptTreeVo);
                }
            }
            parentDeptVo.setChildren(childrenDeptVoList);
            findChildrenToVo(childrenDeptVoList, allDeptList);
        }
    }

    /**
     * 判断菜单是否存在
     *
     * @param sysMenus
     * @param sysMenu
     * @return
     */
    public boolean exists(List<SysMenu> sysMenus, SysMenu sysMenu) {
        boolean exist = false;
        for (SysMenu menu : sysMenus) {
            if (menu.getMenuId().equals(sysMenu.getMenuId())) {
                exist = true;
            }
        }
        return !exist;
    }

    /**
     * 不重复的验证码
     *
     * @param i
     * @return
     */
    public String codeGen(int i) {
        char[] codeSequence = {'Q', 'W', 'E', 'R', 'T', 'Y', 'U', 'I',
                'O', 'P', 'L', 'K', 'J', 'H', 'G', 'F', 'D',
                'S', 'A', 'Z', 'X', 'C', 'V', 'B', 'N', 'M', '1',
                '2', '3', '4', '5', '6', '7', '8', '9', '0'};
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder();
        int count = 0;
        while (true) {
            // 随机产生一个下标，通过下标取出字符数组中对应的字符
            char c = codeSequence[random.nextInt(codeSequence.length)];
            // 假设取出来的字符在动态字符中不存在，代表没有重复的
            if (stringBuilder.indexOf(c + "") == -1) {
                stringBuilder.append(c);
                count++;
                //控制随机生成的个数
                if (count == i) {
                    break;
                }
            }
        }
        return stringBuilder.toString();
    }
}
