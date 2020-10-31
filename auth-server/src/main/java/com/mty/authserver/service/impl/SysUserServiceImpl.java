package com.mty.authserver.service.impl;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mty.authserver.domain.SysUser;
import com.mty.authserver.mapper.SysUserMapper;
import com.mty.authserver.service.ISysUserService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author 蒋老湿
 * @since 2019-04-21
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {
    @Override
    public SysUser findByUserInfoName(String username) {
        SysUser sysUser = baseMapper.selectOne(Wrappers.<SysUser>lambdaQuery()
                .select(SysUser::getUserId, SysUser::getUsername, SysUser::getPhone, SysUser::getEmail, SysUser::getPassword, SysUser::getDeptId,
                        SysUser::getJobId, SysUser::getAvatar, SysUser::getTenantId)
                .eq(SysUser::getUsername, username));

        return sysUser;
    }


}
