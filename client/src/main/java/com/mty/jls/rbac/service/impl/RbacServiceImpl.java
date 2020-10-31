package com.mty.jls.rbac.service.impl;

import com.mty.jls.contract.model.SecurityUser;
import com.mty.jls.rbac.domain.SysMenu;
import com.mty.jls.rbac.service.ISysMenuService;
import com.mty.jls.rbac.service.ISysRoleMenuService;
import com.mty.jls.rbac.service.RbacService;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@Component("rbacService")
public class RbacServiceImpl implements RbacService {
    @Autowired
    private ISysRoleMenuService sysRoleMenuService;
    @Autowired
    private ISysMenuService sysMenuService;
    /**
     * 判断是否匹配
     */
    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    /**
     * 判断当前登录用户是否有该路劲访问权限
     *
     * @param request
     * @param authentication
     * @return
     */
    @Override
    public boolean hasPermission(HttpServletRequest request, Authentication authentication) {
        Object principal = authentication.getPrincipal();

        boolean hasPermission = false;

        if (principal instanceof SecurityUser) {
            var securityUser = ((SecurityUser) principal);
            //如果用户名是admin，就永远返回true
            if ("admin".equals(securityUser.getUsername())) {
                hasPermission = true;
            } else {
                // 读取用户所拥有权限的所有URL
                final List<Integer> menuIds = sysRoleMenuService.getMenuIdByUserId(securityUser.getUserId());
                final List<String> menuPaths = sysMenuService.listByIds(menuIds).stream().map(SysMenu::getPath)
                        .filter(Strings::isNotEmpty).collect(Collectors.toList());
                //循环所有的url,进行授权
                for (String url : menuPaths) {
                    //如果url匹配,进行授权
                    if (antPathMatcher.match(url, request.getRequestURI())) {
                        hasPermission = true;
                        break;
                    }
                }
            }
        }
        return hasPermission;
    }
}
