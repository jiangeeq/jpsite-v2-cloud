package com.mty.jls.rbac.bean;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * 重写 equals 和 hashCode 方法用于对象比较
 * @author jiangpeng
 * @date 2020/10/1311:59
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode
public class ISecurityUser implements Serializable {
    private Integer userId;
    private String username;
    private String password;
    private Long tenantId;
    private List<String> authorities;
}
