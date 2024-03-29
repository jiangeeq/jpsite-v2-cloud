package com.mty.jls.controller.rbac;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dove.jls.common.bean.PageRequest;
import com.mty.jls.code.contract.ValidateCodeProcessor;
import com.mty.jls.contract.exception.BaseException;
import com.mty.jls.contract.exception.BusinessException;
import com.mty.jls.contract.model.Response;
import com.mty.jls.rbac.api.ISysUserService;
import com.mty.jls.rbac.bean.ISysUser;
import com.mty.jls.rbac.bean.IUserDTO;
import com.mty.jls.utils.RbacUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@Api(value = "用户管理", tags = "用户管理")
@RestController
@RequestMapping("/user")
public class SysUserController {

    @Reference(version = "1.0.0", group = "rbac")
    private ISysUserService userService;
    @Autowired
    private ValidateCodeProcessor smsValidateCodeProcessor;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @ApiOperation(value = "保存用户包括角色和部门")
    @PostMapping
    public Response insert(@RequestBody IUserDTO userDto) {
        return Response.succeed(userService.insertUser(userDto));
    }


    @GetMapping
    @ApiOperation(value = "获取用户列表集合")
    public Response getList(Page page, IUserDTO userDTO) {
        PageRequest pageRequest = new PageRequest((int)page.getCurrent(), (int)page.getSize());
        return Response.succeed(userService.getUsersWithRolePage(pageRequest, userDTO));
    }

    @ApiOperation(value = "更新用户包括角色和部门")
    @PutMapping
    public Response update(@RequestBody IUserDTO userDto) {
        return Response.succeed(userService.updateUser(userDto));
    }

    @ApiOperation(value = "删除用户包括角色和部门")
    @DeleteMapping("/{userId}")
    public Response delete(@PathVariable("userId") Integer userId) {
        return Response.succeed(userService.removeUser(userId));
    }


    @ApiOperation(value = "重置密码")
    @PutMapping("/{userId}")
    public Response restPass(@PathVariable("userId") Integer userId) {
        return Response.succeed(userService.restPass(userId, passwordEncoder.encode("123456")));
    }


    @ApiOperation(value = "获取个人信息")
    @GetMapping("/info")
    public Response getUserInfo() {
        return Response.succeed(userService.findByUserInfoName(RbacUtil.getSecurityUser().getUsername()));
    }

    @ApiOperation(value = "修改密码")
    @PutMapping("updatePass")
    public Response updatePass(@RequestParam String oldPass, @RequestParam String newPass) {
        // 校验密码流程
        ISysUser sysUser = userService.findSecurityUserByUser(new ISysUser().setUsername(RbacUtil.getSecurityUser().getUsername()));
        if (!RbacUtil.validatePass(oldPass, sysUser.getPassword())) {
            throw new BaseException("原密码错误");
        }
        if (StrUtil.equals(oldPass, newPass)) {
            throw new BaseException("新密码不能与旧密码相同");
        }
        // 修改密码流程
        ISysUser user = new ISysUser();
        user.setUserId(sysUser.getUserId());
        user.setPassword(RbacUtil.encode(newPass));
        return Response.succeed(userService.updateUserInfo(user));
    }


    @ApiOperation(value = "检测用户名是否存在 避免重复")
    @PostMapping("/vailUserName")
    public Response vailUserName(@RequestParam String userName) {
        ISysUser sysUser = userService.findSecurityUserByUser(new ISysUser().setUsername(userName));
        if (Objects.nonNull(sysUser)) {
            throw new BusinessException("当前用户已存在");
        }
        return Response.succeed(sysUser);
    }


    @ApiOperation(value = "发送邮箱验证码")
    @PostMapping("/sendMailCode")
    public Response sendMailCode(@RequestParam String to, HttpServletRequest request) {
        return Response.succeed(null);
    }


    @ApiOperation(value = "修改邮箱")
    @PutMapping("updateEmail")
    public Response updateEmail(@RequestParam String mail, @RequestParam String code, @RequestParam String pass, HttpServletRequest request) {
        // 校验验证码流程
        smsValidateCodeProcessor.validate(new ServletWebRequest(request));
        var sysUser = userService.findSecurityUserByUser(new ISysUser().setUsername(RbacUtil.getSecurityUser().getUsername()));
        if (!RbacUtil.validatePass(pass, sysUser.getPassword())) {
            throw new BaseException("密码错误");
        }
        // 修改邮箱流程
        var user = new ISysUser();
        user.setUserId(sysUser.getUserId());
        user.setEmail(mail);
        return Response.succeed(userService.updateUserInfo(user));
    }
}
