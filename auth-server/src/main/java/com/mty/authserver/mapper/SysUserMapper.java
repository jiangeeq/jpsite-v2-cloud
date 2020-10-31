package com.mty.authserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mty.authserver.domain.SysUser;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
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

}
