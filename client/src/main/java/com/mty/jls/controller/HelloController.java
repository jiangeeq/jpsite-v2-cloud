package com.mty.jls.controller;

import com.dove.jls.common.utils.JsonUtil;
import com.mty.jls.config.security.bean.CustomWebAuthenticationDetails;
import com.mty.jls.contract.model.Response;
import com.mty.jls.rbac.api.ISysDeptService;
import com.mty.jls.rbac.bean.ISysDept;
import com.mty.jls.utils.RbacUtil;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author jiangpeng
 * @date 2020/10/1310:11
 */
@RestController
public class HelloController {
    @Reference(version = "1.0.0")
    private ISysDeptService iSysDeptService;

    @GetMapping("/deptList")
    private String deptList(){
        List<ISysDept> iSysDepts = iSysDeptService.selectDeptList();
        return JsonUtil.encode(iSysDepts);
    }

    @GetMapping("/hello")
    public String hello() {
        return "hello jpsite-v2";
    }

    @GetMapping("/filterError")
    public void throwException(HttpServletRequest request) throws Exception {
        throw (Exception) request.getAttribute("exception");
    }

    @GetMapping("/authenticationDetail")
    public Response authenticationDetail(HttpServletRequest request) throws Exception {
        final CustomWebAuthenticationDetails customDetail = RbacUtil.customDetail();
        return Response.succeed(customDetail);
    }
}

