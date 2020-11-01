package com.mty.rbac.api;



import com.mty.rbac.bean.IDictDTO;
import com.mty.rbac.bean.ISysDict;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 字典表 服务类
 * </p>
 *
 * @author 蒋老湿
 * @since 2019-05-17
 */
public interface ISysDictService  {
    /**
     * 分页查询字典列表
     * @param page
     * @param pageSize
     * @return
     */
//    IPage<SysDict> selectDictList(int page, int pageSize);

    /**
     * 修改字典
     * @param dictDto
     * @return
     */
    Boolean updateDict(IDictDTO dictDto);


    /**
     *
     * @param name
     * @return
     */
    List<ISysDict> selectDictDetailList(String name);

    /**
     * 根据字典名称删除
     * @param name
     * @return
     */
    Boolean deleteDictByName(String name);


    /**
     * 根据主键Id删除字典
     * @param id
     * @return
     */
    Boolean removeById(Integer id);
}
