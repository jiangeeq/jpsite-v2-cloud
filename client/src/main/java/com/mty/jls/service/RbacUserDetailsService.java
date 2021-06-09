package com.mty.jls.service;


import com.mty.jls.contract.model.SecurityUser;
import com.dove.jls.common.utils.BeanPlusUtil;
import com.mty.jls.rbac.api.ISysUserService;
import com.mty.jls.rbac.bean.ISysUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Set;

/**
 * @author 掘金-蒋老湿（公众号：十分钟学编程）
 */
@Component("userDetailsService")
@Transactional(rollbackFor = Exception.class)
@Slf4j
@EnableAsync
public class RbacUserDetailsService implements UserDetailsService {
    @Reference(version = "1.0.0", group = "rbac")
    private ISysUserService sysUserService;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("====>表单登录用户名[{}]", username);
        ISysUser user = sysUserService.findByUserInfoName(username);
        final SecurityUser securityUser = BeanPlusUtil.copySingleProperties(user, SecurityUser::new);
        securityUser.setAuthorities(getUserAuthorities(user.getUserId()));
        return securityUser;
    }

    /**
     * 根据用户Id获取权限
     *
     * @param userId
     * @return
     */
    private Collection<? extends GrantedAuthority> getUserAuthorities(int userId) {
        // 根据用户拥有的权限标识与如 @PreAuthorize("hasAuthority('sys:menu:view')") 标注的接口对比，决定是否可以调用接口
        // 权限集合
        Set<String> permissions = sysUserService.findPermsByUserId(userId);
        // 角色id集合
        Set<String> roleIds = sysUserService.findRoleIdByUserId(userId);
        permissions.addAll(roleIds);
        return AuthorityUtils.createAuthorityList(permissions.toArray(new String[0]));
    }
}
