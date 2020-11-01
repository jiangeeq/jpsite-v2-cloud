package com.mty.rbac.api;


import com.mty.rbac.bean.IDeptDTO;
import com.mty.rbac.bean.IDeptTreeVo;
import com.mty.rbac.bean.ISysDept;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 部门管理 服务类
 * </p>
 *
 * @author 蒋老湿
 * @since 2019-04-21
 */
public interface ISysDeptService  {

    /**
     * 查询部门信息
     * @return
     */
    List<ISysDept> selectDeptList();

    /**
     * 更新部门
     * @param entity
     * @return
     */
    boolean updateDeptById(IDeptDTO entity);

    /**
     * 删除部门
     * @param id
     * @return
     */
    boolean removeById(Serializable id);

    /**
     * 根据部门id查询部门名称
     * @param deptId
     * @return
     */
    String selectDeptNameByDeptId(int deptId);

    /**
     * 通过此部门id查询于此相关的部门ids
     * @param deptId
     * @return
     */
    List<Integer> selectDeptIds(int deptId);

    /**
     * 获取部门树
     * @return
     */
    List<IDeptTreeVo> getDeptTree();


}
