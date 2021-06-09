package com.mty.jls.rbac.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dove.jls.common.bean.PageRequest;
import com.dove.jls.common.exception.BusinessException;
import com.dove.jls.common.utils.BeanPlusUtil;
import com.mty.jls.rbac.api.ISysDeptService;
import com.mty.jls.rbac.api.ISysJobService;
import com.mty.jls.rbac.api.ISysMenuService;
import com.mty.jls.rbac.api.ISysUserRoleService;
import com.mty.jls.rbac.api.ISysUserService;
import com.mty.jls.rbac.bean.IPageResponse;
import com.mty.jls.rbac.bean.ISysUser;
import com.mty.jls.rbac.bean.IUserDTO;
import com.mty.jls.rbac.domain.SysUser;
import com.mty.jls.rbac.domain.SysUserRole;
import com.mty.jls.rbac.dto.UserDTO;
import com.mty.jls.rbac.mapper.SysUserMapper;
import com.mty.jls.rbac.mapper.SysUserRoleMapper;
import org.apache.dubbo.config.annotation.Service;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author 蒋老湿
 * @since 2019-04-21
 */
@Service(cluster = "failsafe",
        loadbalance = "roundrobin",
        group = "rbac",
        version = "1.0.0"
)
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {
    @Autowired
    private SysUserRoleMapper userRoleMapper;
    @Autowired
    private ISysUserRoleService userRoleService;
    @Autowired
    private ISysDeptService deptService;
    @Autowired
    private ISysJobService jobService;
    @Autowired
    private ISysMenuService menuService;

    @Override
    public IPageResponse<List<ISysUser>> getUsersWithRolePage(PageRequest pageRequest, IUserDTO userDTO) {

        if (ObjectUtil.isNotNull(userDTO) && Objects.nonNull(userDTO.getDeptId()) && userDTO.getDeptId() != 0) {
            userDTO.setDeptList(deptService.selectDeptIds(userDTO.getDeptId()));
        }
        final UserDTO dto = BeanPlusUtil.copySingleProperties(userDTO, UserDTO::new);

        final Page page = new Page(pageRequest.getPageSize(), pageRequest.getPageNumber());

        final IPage<SysUser> userVosPage = baseMapper.getUserVosPage(page, dto);

        IPageResponse<List<ISysUser>> iPageResponse = IPageResponse.ok();
        iPageResponse.setTotal(userVosPage.getTotal()).setPageSize(userVosPage.getSize())
                .setRecords(BeanPlusUtil.copyListProperties(userVosPage.getRecords(), ISysUser::new))
                .setPage(userVosPage.getCurrent());
        return iPageResponse;
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean insertUser(IUserDTO userDto) {
        SysUser sysUser = BeanPlusUtil.copySingleProperties(userDto, SysUser::new, (s, t) -> t.setPassword(userDto.getPassword()));
        baseMapper.insertUser(sysUser);

        saveUserRole(sysUser.getUserId(), userDto.getRoleList());
        return true;
    }

    private void saveUserRole(int userId, List<Integer> userRoleList) {
        List<SysUserRole> userRoles = userRoleList.stream().map(role -> {
            SysUserRole sysUserRole = new SysUserRole();
            sysUserRole.setRoleId(role);
            sysUserRole.setUserId(userId);
            return sysUserRole;
        }).collect(Collectors.toList());
        userRoleMapper.saveBatch(userRoles);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateUser(IUserDTO userDto) {
        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(userDto, sysUser);
        baseMapper.updateById(sysUser);
        userRoleMapper.delete(Wrappers.<SysUserRole>lambdaQuery().eq(SysUserRole::getUserId, sysUser.getUserId()));
        saveUserRole(sysUser.getUserId(), userDto.getRoleList());
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean removeUser(Integer userId) {
        baseMapper.deleteById(userId);
        return userRoleMapper.delete(Wrappers.<SysUserRole>lambdaQuery().eq(SysUserRole::getUserId, userId)) > 0;
    }

    @Override
    public boolean restPass(Integer userId, String password) {
        return baseMapper.updateById(new SysUser().setPassword(password).setUserId(userId)) > 0;
    }

    @Override
    public ISysUser findByUserInfoName(String username) {
        SysUser sysUser = baseMapper.selectOne(Wrappers.<SysUser>lambdaQuery()
                .select(SysUser::getUserId, SysUser::getUsername, SysUser::getPhone, SysUser::getEmail, SysUser::getPassword, SysUser::getDeptId,
                        SysUser::getJobId, SysUser::getAvatar, SysUser::getTenantId)
                .eq(SysUser::getUsername, username));
        // 获取部门
        sysUser.setDeptName(deptService.selectDeptNameByDeptId(sysUser.getDeptId()));
        // 获取岗位
        sysUser.setJobName(jobService.selectJobNameByJobId(sysUser.getJobId()));
        return BeanPlusUtil.copySingleProperties(sysUser, ISysUser::new);
    }

    @Override
    public Set<String> findPermsByUserId(Integer userId) {
        return menuService.findPermsByUserId(userId).stream().filter(Strings::isNotEmpty).collect(Collectors.toSet());
    }

    @Override
    public Set<String> findRoleIdByUserId(Integer userId) {
        return userRoleService
                .selectUserRoleListByUserId(userId)
                .stream()
                .map(sysUserRole -> "ROLE_" + sysUserRole.getRoleId())
                .collect(Collectors.toSet());
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean register(IUserDTO userDTO) {
        // 查询用户名是否存在
        SysUser byUserInfoName = findUserByIdOrNameOrPhone(userDTO.getUsername());
        if (ObjectUtil.isNotNull(byUserInfoName)) {
            throw new BusinessException("账户名已被注册");
        }
        SysUser securityUser = findUserByIdOrNameOrPhone(userDTO.getPhone());
        if (ObjectUtil.isNotNull(securityUser)) {
            throw new BusinessException("手机号已被注册");
        }

        SysUser sysUser = BeanPlusUtil.copySingleProperties(userDTO, SysUser::new, (s, t) -> {
            t.setDeptId(6);
            t.setJobId(4);
            t.setLockFlag("0");
            t.setPassword(userDTO.getPassword());
        });
        baseMapper.insertUser(sysUser);

        SysUserRole sysUserRole = new SysUserRole();
        sysUserRole.setRoleId(14);
        sysUserRole.setUserId(sysUser.getUserId());
        return userRoleMapper.insert(sysUserRole) > 0;
    }

    @Override
    public boolean updateUserInfo(ISysUser sysUser) {
        final SysUser user = BeanPlusUtil.copySingleProperties(sysUser, SysUser::new);
        return baseMapper.updateById(user) > 0;
    }

    @Override
    public ISysUser findSecurityUserByUser(ISysUser sysUser) {
        LambdaQueryWrapper<SysUser> select = Wrappers.<SysUser>lambdaQuery()
                .select(SysUser::getUserId, SysUser::getUsername, SysUser::getPassword);
        if (StrUtil.isNotEmpty(sysUser.getUsername())) {
            select.eq(SysUser::getUsername, sysUser.getUsername());
        } else if (StrUtil.isNotEmpty(sysUser.getPhone())) {
            select.eq(SysUser::getPhone, sysUser.getPhone());
        } else if (ObjectUtil.isNotNull(sysUser.getUserId()) && sysUser.getUserId() != 0) {
            select.eq(SysUser::getUserId, sysUser.getUserId());
        }
        final SysUser user = baseMapper.selectOne(select);

        return BeanPlusUtil.copySingleProperties(user, ISysUser::new);
    }

    private SysUser findUserByIdOrNameOrPhone(String userIdOrUserNameOrPhone) {
        LambdaQueryWrapper<SysUser> select = Wrappers.<SysUser>lambdaQuery()
                .select(SysUser::getUserId, SysUser::getUsername, SysUser::getPassword)
                .eq(SysUser::getUsername, userIdOrUserNameOrPhone)
                .or()
                .eq(SysUser::getPhone, userIdOrUserNameOrPhone)
                .or()
                .eq(SysUser::getUserId, userIdOrUserNameOrPhone);
        return baseMapper.selectOne(select);
    }
}
