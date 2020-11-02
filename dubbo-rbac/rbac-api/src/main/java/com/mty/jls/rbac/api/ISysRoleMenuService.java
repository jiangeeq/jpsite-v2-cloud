package com.mty.jls.rbac.api;



import java.util.List;

/**
 * <p>
 * 角色菜单表 服务类
 * </p>
 *
 * @author 蒋老湿
 * @since 2019-04-21
 */
public interface ISysRoleMenuService {
    List<Integer> getMenuIdByUserId(Integer userId);


}
