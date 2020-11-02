package com.mty.jls.rbac.api;



import com.mty.jls.rbac.bean.ISysRoleDept;

import java.util.List;

/**
 * <p>
 * 角色与部门对应关系 服务类
 * </p>
 *
 * @author 蒋老湿
 * @since 2019-04-21
 */
public interface ISysRoleDeptService  {

    /**
     * 根据角色id查询部门ids
     * @param roleId
     * @return
     */
    List<ISysRoleDept> getRoleDeptIds(int roleId);
}
