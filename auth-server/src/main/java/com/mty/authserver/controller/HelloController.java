package com.mty.authserver.controller;

import com.dove.jls.common.utils.JsonUtil;
import com.mty.jls.rbac.api.ISysUserService;
import com.mty.jls.rbac.bean.ISysUser;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author jiangpeng
 * @date 2020/10/1614:46
 */
@RestController
public class HelloController {
    @Reference(version = "1.0.0")
    private ISysUserService sysUserService;

    @GetMapping("/hello")
    public String hello() {
        return "这里是统一授权中心首页，您好！";
    }

    @PostMapping("/hello2")
    public String hello2() {
        return "这里是统一授权中心首页，您好！123";
    }

    @GetMapping("/user")
    public ISysUser getUser(HttpServletRequest request) {
        final String jwt = request.getHeader("Authorization").replace("bearer ", "").replace("Bearer ", "");
        final Jwt decode = JwtHelper.decode(jwt);
        final Map<String, String> map = JsonUtil.toMap(decode.getClaims(), JsonUtil.StringObjectMap);
        final ISysUser sysUser = sysUserService.findByUserInfoName(map.get("user_name"));
        sysUser.setPassword("");
        return sysUser;
    }

    @GetMapping("/principal")
    public String principal(HttpServletRequest request) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return principal.toString();
    }
}
