package com.mty.jls.rbac.api;


import com.mty.jls.rbac.bean.ISysUser;
import com.mty.jls.rbac.bean.IUserDTO;

import java.util.Set;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author 蒋老湿
 * @since 2019-04-21
 */
public interface ISysUserService  {

    /**
     * 分页查询用户信息（含有角色信息）
     *
     * @param page    分页对象
     * @param userDTO 参数列表
     * @return
     */
//    IPage<SysUser> getUsersWithRolePage(Page page, UserDTO userDTO);

    /**
     * 保存用户以及角色部门等信息
     * @param userDto
     * @return
     */
    boolean insertUser(IUserDTO userDto);

    /**
     * 更新用户以及角色部门等信息
     * @param userDto
     * @return
     */
    boolean updateUser(IUserDTO userDto);

    /**
     * 删除用户信息
     * @param userId
     * @return
     */
    boolean removeUser(Integer userId);

    /**
     * 重置密码
     * @param userId
     * @return
     */
    boolean restPass(Integer userId);

    /**
     * 通过用户名查找用户个人信息
     *
     * @param username 用户名
     * @return 用户信息
     */
    ISysUser findByUserInfoName(String username);

    /**
     * 根据用户id查询权限
     * @param userId
     * @return
     */
    Set<String> findPermsByUserId(Integer userId);

    /**
     * 通过用户id查询角色集合
     * @param userId
     * @return
     */
    Set<String> findRoleIdByUserId(Integer userId);

    /**
     * 注册用户
     * @return
     */
    boolean register(IUserDTO userDTO);

    /**
     * 修改用户信息
     * @param sysUser
     * @return
     */
    boolean updateUserInfo(ISysUser sysUser);

    /**
     * 通过用户去查找用户(id/用户名/手机号)
     * @param sysUser
     * @return
     */
    ISysUser findSecurityUserByUser(ISysUser sysUser);
}
