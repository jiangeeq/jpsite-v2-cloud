package com.mty.jls.rbac.service.impl;

import com.mty.jls.rbac.api.IRbacService;
import com.mty.jls.rbac.api.ISysRoleMenuService;
import com.mty.jls.rbac.bean.ISecurityUser;
import com.mty.jls.rbac.domain.SysMenu;
import com.mty.jls.rbac.mapper.SysMenuMapper;
import org.apache.dubbo.config.annotation.Service;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.AntPathMatcher;

import java.util.List;
import java.util.stream.Collectors;

@Service(cluster = "failsafe",
        loadbalance = "roundrobin",
        group = "rbac",
        version = "1.0.0"
)
public class RbacServiceImpl implements IRbacService {
    @Autowired
    private ISysRoleMenuService sysRoleMenuService;
    @Autowired
    private SysMenuMapper sysMenuMapper;

    /**
     * 判断是否匹配
     */
    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    /**
     * 判断当前登录用户是否有该路劲访问权限
     *
     * @param requestUri
     * @param securityUser
     * @return
     */
    @Override
    public boolean hasPermission(String requestUri, ISecurityUser securityUser) {
        boolean hasPermission = false;

        //如果用户名是admin，就永远返回true
        if ("admin".equals(securityUser.getUsername())) {
            hasPermission = true;
        } else {
            // 读取用户所拥有权限的所有URL
            final List<Integer> menuIds = sysRoleMenuService.getMenuIdByUserId(securityUser.getUserId());
            final List<String> menuPaths = sysMenuMapper.selectBatchIds(menuIds).stream().map(SysMenu::getPath)
                    .filter(Strings::isNotEmpty).collect(Collectors.toList());
            //循环所有的url,进行授权
            for (String url : menuPaths) {
                //如果url匹配,进行授权
                if (antPathMatcher.match(url, requestUri)) {
                    hasPermission = true;
                    break;
                }
            }
        }

        return hasPermission;
    }
}
