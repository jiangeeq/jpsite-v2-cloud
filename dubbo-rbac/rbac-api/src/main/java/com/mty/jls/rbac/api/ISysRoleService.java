package com.mty.jls.rbac.api;


import com.mty.jls.rbac.bean.ISysMenu;
import com.mty.jls.rbac.bean.IRoleDTO;
import com.mty.jls.rbac.bean.ISysRole;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 系统角色表 服务类
 * </p>
 *
 * @author 蒋老湿
 * @since 2019-04-21
 */
public interface ISysRoleService {

    /**
     * 保存角色和菜单
     * @param roleDto
     * @return
     */
    boolean saveRoleMenu(IRoleDTO roleDto);

    /**
     * 更新角色和菜单
     * @param roleDto
     * @return
     */
    boolean updateRoleMenu(IRoleDTO roleDto);

    /**
     * 根据主键删除角色
     * @param id
     * @return
     */
    boolean removeById(Serializable id);

    /**
     * 获取角色列表
     * @return
     */
    List<ISysRole> selectRoleList(String roleName);

    /**
     * 根据角色id获取菜单
     * @param roleId
     * @return
     */
    List<ISysMenu> findMenuListByRoleId(int roleId);

    /**
     * 通过用户ID，查询角色信息
     *
     * @param userId
     * @return
     */
    List<ISysRole> findRolesByUserId(Integer userId);
}
