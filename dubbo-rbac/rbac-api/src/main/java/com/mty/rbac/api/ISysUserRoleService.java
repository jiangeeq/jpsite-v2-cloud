package com.mty.rbac.api;


import com.mty.rbac.bean.ISysUserRole;

import java.util.List;

/**
 * <p>
 * 用户角色表 服务类
 * </p>
 *
 * @author 蒋老湿
 * @since 2019-04-21
 */
public interface ISysUserRoleService  {


    /**
     * 根据用户id查询用户角色关系
     * @param userId
     * @return
     */
    List<ISysUserRole> selectUserRoleListByUserId(Integer userId);
}
