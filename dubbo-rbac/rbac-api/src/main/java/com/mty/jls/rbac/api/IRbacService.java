package com.mty.jls.rbac.api;

import com.mty.jls.rbac.bean.ISecurityUser;
import javax.servlet.http.HttpServletRequest;

public interface IRbacService {

    /**
     * 判断的是否拥有该路径url权限
     * @param requestUri
     * @param authentication
     * @return
     */
	boolean hasPermission(String requestUri, ISecurityUser authentication);

}
