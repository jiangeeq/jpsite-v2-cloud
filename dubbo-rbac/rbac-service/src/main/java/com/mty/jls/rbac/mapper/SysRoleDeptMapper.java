package com.mty.jls.rbac.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mty.jls.rbac.domain.SysRoleDept;

import java.util.List;

/**
 * <p>
 * 角色与部门对应关系 Mapper 接口
 * </p>
 *
 * @author 蒋老湿
 * @since 2019-04-21
 */
public interface SysRoleDeptMapper extends BaseMapper<SysRoleDept> {

    void saveBatch(List<SysRoleDept> list);

}
