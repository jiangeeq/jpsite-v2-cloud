package com.mty.jls.business.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author jiangpeng
 * @date 2020/10/2214:12
 */
@AllArgsConstructor
@Getter
public enum OperationTypeEnum {
    RECHARGE(1, "充值", "C"),
    CONSUME(2, "消费", "X"),
    WITHDRAW(3, "提现", "T"),
    EDIT_PASSWORD(4, "修改密码", "P");

    private Integer code;
    private String name;
    private String identify;
}
