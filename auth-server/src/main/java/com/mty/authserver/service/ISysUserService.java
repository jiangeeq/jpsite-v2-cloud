package com.mty.authserver.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mty.authserver.domain.SysUser;

import java.util.Set;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author 蒋老湿
 * @since 2019-04-21
 */
public interface ISysUserService extends IService<SysUser> {
    /**
     * 通过用户名查找用户个人信息
     *
     * @param username 用户名
     * @return 用户信息
     */
    SysUser findByUserInfoName(String username);
}
