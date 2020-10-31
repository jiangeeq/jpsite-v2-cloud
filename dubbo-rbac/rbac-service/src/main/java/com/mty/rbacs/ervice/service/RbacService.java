package com.mty.jls.rbac.service;

import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;

public interface RbacService {

    /**
     * 判断的是否拥有该路径url权限
     * @param request
     * @param authentication
     * @return
     */
	boolean hasPermission(HttpServletRequest request, Authentication authentication);

}
