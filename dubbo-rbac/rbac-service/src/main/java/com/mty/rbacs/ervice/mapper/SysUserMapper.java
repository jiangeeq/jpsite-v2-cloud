package com.mty.jls.rbac.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.mty.jls.config.datascope.DataScope;
import com.mty.jls.rbac.domain.SysUser;
import com.mty.jls.rbac.dto.UserDTO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author 蒋老湿
 * @since 2019-04-21
 */
@Repository
public interface SysUserMapper extends BaseMapper<SysUser> {
    @Insert("INSERT INTO sys_user (username, PASSWORD, dept_id, job_id, phone , email, avatar, lock_flag) VALUES (#{username}, #{password}, #{deptId}, #{jobId}, #{phone} , #{email}, #{avatar}, #{lockFlag})")
    @Options(useGeneratedKeys = true, keyProperty = "userId", keyColumn = "user_id")
    boolean insertUser(SysUser sysUser);

    /**
     * 分页查询用户信息（含角色）
     *
     * @param page      分页
     * @param userDTO   查询参数
     * @param dataScope
     * @return list
     */
    IPage<SysUser> getUserVosPage(Page page, @Param("query") UserDTO userDTO, DataScope dataScope);

}
