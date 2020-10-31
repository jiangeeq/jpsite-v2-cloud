package com.mty.jls.contract.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @Classname MenuConstant
 * @Description 菜单常量
 */
public class MenuConstant {

    /**
     * 菜单类型
     */
    @AllArgsConstructor
    @Getter
    public enum MenuType {
        /**
         * 目录
         */
        CATALOG(0),
        /**
         * 菜单
         */
        MENU(1),
        /**
         * 按钮
         */
        BUTTON(2);

        private int value;
    }

}
