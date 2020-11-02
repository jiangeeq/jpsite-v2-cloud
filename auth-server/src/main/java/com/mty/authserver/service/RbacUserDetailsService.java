package com.mty.authserver.service;


import com.mty.authserver.domain.SysUser;
import com.mty.authserver.domain.SecurityUser;
import com.dove.jls.common.utils.BeanPlusUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Objects;

/**
 * @author 掘金-蒋老湿（公众号：十分钟学编程）
 */
@Component
@Transactional(rollbackFor = Exception.class)
@Slf4j
@EnableAsync
public class RbacUserDetailsService implements UserDetailsService {
    @Autowired
    private ISysUserService sysUserService;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("====>表单登录用户名[{}]", username);
        SysUser user = sysUserService.findByUserInfoName(username);
        if (Objects.isNull(user)) {
            throw new UsernameNotFoundException("当前用户不存在");
        }
        final SecurityUser securityUser = BeanPlusUtil.copySingleProperties(user, SecurityUser::new);
        securityUser.setAuthorities(getUserAuthorities(user.getUserId()));
        return securityUser;
    }

    /**
     * 封装 根据用户Id获取权限
     *
     * @param userId
     * @return
     */
    private Collection<? extends GrantedAuthority> getUserAuthorities(int userId) {
        // 获取用户拥有的角色
        // 用户权限列表，根据用户拥有的权限标识与如 @PreAuthorize("hasAuthority('sys:menu:view')") 标注的接口对比，决定是否可以调用接口
        // 权限集合
//        Set<String> permissions = sysUserService.findPermsByUserId(userId);
//        // 角色集合
//        Set<String> roleIds = sysUserService.findRoleIdByUserId(userId);
//        permissions.addAll(roleIds);
//        return AuthorityUtils.createAuthorityList(permissions.toArray(new String[0]));
        return AuthorityUtils.createAuthorityList("admin");

    }
}
