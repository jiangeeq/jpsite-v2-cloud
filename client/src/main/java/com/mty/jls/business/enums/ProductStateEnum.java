package com.mty.jls.business.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author jiangpeng
 * @date 2020/10/1211:11
 */
@AllArgsConstructor
@Getter
public enum ProductStateEnum {
    SALED(1, "销售中"),
    STOP_SALED(2, "停售中");

    private Integer code;
    private String name;

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
