package com.mty.jls.rbac.service;

import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mty.jls.contract.model.Response;
import com.mty.jls.rbac.domain.SysMenu;
import com.mty.jls.rbac.dto.MenuDTO;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 菜单权限表 服务类
 * </p>
 *
 * @author 蒋老湿
 * @since 2019-04-21
 */
public interface ISysMenuService extends IService<SysMenu> {

    /**
     * 更新菜单信息
     * @param entity
     * @return
     */
    boolean updateMenuById(MenuDTO entity);

    /**
     * 删除菜单信息
     * @param id
     * @return
     */
    boolean removeMenuById(Serializable id);

    /**
     * 根据用户id查找菜单树
     * @return
     */
    List<SysMenu> selectMenuTree(Integer uid);

    /**
     * @Author 蒋老湿
     * @Description 根据父id查询菜单
     * @Date 18:43 2019-05-12
     **/
    SysMenu getMenuById(Integer parentId);

    /**
     * @Description 根据用户id查询权限
     **/
    List<String> findPermsByUserId(Integer userId);
}
