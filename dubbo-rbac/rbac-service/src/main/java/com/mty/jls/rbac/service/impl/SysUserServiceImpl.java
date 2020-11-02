//package com.mty.jls.rbac.service.impl;
//
//import cn.hutool.core.bean.BeanUtil;
//import cn.hutool.core.util.ObjectUtil;
//import cn.hutool.core.util.StrUtil;
//import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
//import com.baomidou.mybatisplus.core.metadata.IPage;
//import com.baomidou.mybatisplus.core.toolkit.Wrappers;
//import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
//import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
//import com.mty.jls.config.datascope.DataScope;
//import com.mty.jls.contract.exception.BaseException;
//import com.mty.jls.dovecommon.utils.BeanPlusUtil;
//import com.mty.jls.rbac.domain.SysUser;
//import com.mty.jls.rbac.domain.SysUserRole;
//import com.mty.jls.rbac.dto.UserDTO;
//import com.mty.jls.rbac.mapper.SysUserMapper;
//import com.mty.jls.rbac.service.ISysJobService;
//import com.mty.jls.rbac.service.ISysUserRoleService;
//import com.mty.jls.utils.RbacUtil;
//import com.mty.rbac.api.ISysDeptService;
//import com.mty.rbac.api.ISysMenuService;
//import com.mty.rbac.api.ISysUserService;
//import org.apache.logging.log4j.util.Strings;
//import org.springframework.beans.BeanUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.AccountExpiredException;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.security.authentication.DisabledException;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//import java.util.Objects;
//import java.util.Set;
//import java.util.stream.Collectors;
//
///**
// * <p>
// * 用户表 服务实现类
// * </p>
// *
// * @author 蒋老湿
// * @since 2019-04-21
// */
//@Service
//public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {
//
//    @Autowired
//    private ISysUserRoleService userRoleService;
//    @Autowired
//    private ISysDeptService deptService;
//    @Autowired
//    private ISysJobService jobService;
//    @Autowired
//    private ISysMenuService menuService;
//    @Autowired
//    private AuthenticationManager authenticationManager;
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    @Override
//    public IPage<SysUser> getUsersWithRolePage(Page page, UserDTO userDTO) {
//
//        if (ObjectUtil.isNotNull(userDTO) && Objects.nonNull(userDTO.getDeptId()) && userDTO.getDeptId() != 0 ) {
//            userDTO.setDeptList(deptService.selectDeptIds(userDTO.getDeptId()));
//        }
//        return baseMapper.getUserVosPage(page, userDTO, new DataScope());
//    }
//
//
//    @Transactional(rollbackFor = Exception.class)
//    @Override
//    public boolean insertUser(UserDTO userDto) {
//        SysUser sysUser = BeanPlusUtil.copySingleProperties(userDto, SysUser::new, (s, t) -> t.setPassword(RbacUtil.encode("123456")));
//        baseMapper.insertUser(sysUser);
//
//        saveUserRole(sysUser.getUserId(), userDto.getRoleList());
//        return true;
//    }
//
//    private void saveUserRole(int userId, List<Integer> userRoleList) {
//        List<SysUserRole> userRoles = userRoleList.stream().map(role -> {
//            SysUserRole sysUserRole = new SysUserRole();
//            sysUserRole.setRoleId(role);
//            sysUserRole.setUserId(userId);
//            return sysUserRole;
//        }).collect(Collectors.toList());
//        userRoleService.saveBatch(userRoles);
//    }
//
//    @Transactional(rollbackFor = Exception.class)
//    @Override
//    public boolean updateUser(UserDTO userDto) {
//        SysUser sysUser = new SysUser();
//        BeanUtils.copyProperties(userDto, sysUser);
//        baseMapper.updateById(sysUser);
//        userRoleService.remove(Wrappers.<SysUserRole>lambdaQuery().eq(SysUserRole::getUserId, sysUser.getUserId()));
//        saveUserRole(sysUser.getUserId(), userDto.getRoleList());
//        return true;
//    }
//
//    @Transactional(rollbackFor = Exception.class)
//    @Override
//    public boolean removeUser(Integer userId) {
//        baseMapper.deleteById(userId);
//        return userRoleService.remove(Wrappers.<SysUserRole>lambdaQuery().eq(SysUserRole::getUserId, userId));
//    }
//
//    @Override
//    public boolean restPass(Integer userId) {
//        return baseMapper.updateById(new SysUser().setPassword(passwordEncoder.encode("123456")).setUserId(userId)) > 0;
//    }
//
//    @Override
//    public SysUser findByUserInfoName(String username) {
//        SysUser sysUser = baseMapper.selectOne(Wrappers.<SysUser>lambdaQuery()
//                .select(SysUser::getUserId, SysUser::getUsername, SysUser::getPhone, SysUser::getEmail, SysUser::getPassword, SysUser::getDeptId,
//                        SysUser::getJobId, SysUser::getAvatar, SysUser::getTenantId)
//                .eq(SysUser::getUsername, username));
//        // 获取部门
//        sysUser.setDeptName(deptService.selectDeptNameByDeptId(sysUser.getDeptId()));
//        // 获取岗位
//        sysUser.setJobName(jobService.selectJobNameByJobId(sysUser.getJobId()));
//        return sysUser;
//    }
//
//    @Override
//    public Set<String> findPermsByUserId(Integer userId) {
//        return menuService.findPermsByUserId(userId).stream().filter(Strings::isNotEmpty).collect(Collectors.toSet());
//    }
//
//    @Override
//    public Set<String> findRoleIdByUserId(Integer userId) {
//        return userRoleService
//                .selectUserRoleListByUserId(userId)
//                .stream()
//                .map(sysUserRole -> "ROLE_" + sysUserRole.getRoleId())
//                .collect(Collectors.toSet());
//    }
//
//
//    @Transactional(rollbackFor = Exception.class)
//    @Override
//    public boolean register(UserDTO userDTO) {
//        // 查询用户名是否存在
//        SysUser byUserInfoName = findUserByIdOrNameOrPhone(userDTO.getUsername());
//        if (ObjectUtil.isNotNull(byUserInfoName)) {
//            throw new BaseException("账户名已被注册");
//        }
//        SysUser securityUser = findUserByIdOrNameOrPhone(userDTO.getPhone());
//        if (ObjectUtil.isNotNull(securityUser)) {
//            throw new BaseException("手机号已被注册");
//        }
//
//        SysUser sysUser = BeanPlusUtil.copySingleProperties(userDTO, SysUser::new, (s, t) -> {
//            t.setDeptId(6);
//            t.setJobId(4);
//            t.setLockFlag("0");
//            t.setPassword(RbacUtil.encode(userDTO.getPassword()));
//        });
//        baseMapper.insertUser(sysUser);
//
//        SysUserRole sysUserRole = new SysUserRole();
//        sysUserRole.setRoleId(14);
//        sysUserRole.setUserId(sysUser.getUserId());
//        return userRoleService.save(sysUserRole);
//    }
//
//    @Override
//    public boolean updateUserInfo(SysUser sysUser) {
//        return baseMapper.updateById(sysUser) > 0;
//    }
//
//    @Override
//    public SysUser findSecurityUserByUser(SysUser sysUser) {
//        LambdaQueryWrapper<SysUser> select = Wrappers.<SysUser>lambdaQuery()
//                .select(SysUser::getUserId, SysUser::getUsername, SysUser::getPassword);
//        if (StrUtil.isNotEmpty(sysUser.getUsername())) {
//            select.eq(SysUser::getUsername, sysUser.getUsername());
//        } else if (StrUtil.isNotEmpty(sysUser.getPhone())) {
//            select.eq(SysUser::getPhone, sysUser.getPhone());
//        } else if (ObjectUtil.isNotNull(sysUser.getUserId()) && sysUser.getUserId() != 0) {
//            select.eq(SysUser::getUserId, sysUser.getUserId());
//        }
//        return baseMapper.selectOne(select);
//    }
//
//    private SysUser findUserByIdOrNameOrPhone(String userIdOrUserNameOrPhone) {
//        LambdaQueryWrapper<SysUser> select = Wrappers.<SysUser>lambdaQuery()
//                .select(SysUser::getUserId, SysUser::getUsername, SysUser::getPassword)
//                .eq(SysUser::getUsername, userIdOrUserNameOrPhone)
//                .or()
//                .eq(SysUser::getPhone, userIdOrUserNameOrPhone)
//                .or()
//                .eq(SysUser::getUserId, userIdOrUserNameOrPhone);
//        return baseMapper.selectOne(select);
//    }
//}
