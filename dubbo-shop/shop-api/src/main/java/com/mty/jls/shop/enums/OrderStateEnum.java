package com.mty.jls.shop.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author jiangpeng
 * @date 2020/10/2211:52
 */
@AllArgsConstructor
@Getter
public enum OrderStateEnum {
    PENDING(1, "待处理"),
    PROCESSING(2, "处理中"),
    SUCCESS(3, "处理成功"),
    FAIL(4, "处理失败");

    private Integer code;
    private String name;
}
