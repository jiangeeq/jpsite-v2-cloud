package com.dove.jls.common.bean;

import cn.hutool.core.util.ArrayUtil;
import lombok.Data;

import java.io.Serializable;

/**
 * @author jiangpeng
 * @date 2020/11/219:23
 */
@Data
public class PageRequest implements Serializable {
    public static final int DEFAULT_PAGE_SIZE = 20;

    /** 页码 */
    private int pageNumber;
    /** 每页结果数 */
    private int pageSize;
    /** 排序 */
    private Order[] orders;

    // ---------------------------------------------------------- Constructor start
    /**
     * 构造
     *
     * @param pageNumber 页码
     * @param pageSize 每页结果数
     */
    public PageRequest(int pageNumber, int pageSize) {
        this.pageNumber = pageNumber < 0 ? 0 : pageNumber;
        this.pageSize = pageSize <= 0 ? DEFAULT_PAGE_SIZE : pageSize;
    }

    /**
     * 构造
     *
     * @param pageNumber 页码
     * @param numPerPage 每页结果数
     * @param order 排序对象
     */
    public PageRequest(int pageNumber, int numPerPage, Order order) {
        this(pageNumber, numPerPage);
        this.orders = new Order[]{order};
    }

    /**
     * 设置排序
     *
     * @param orders 排序
     */
    public void addOrder(Order... orders) {
        if(null != this.orders){
            ArrayUtil.append(this.orders, orders);
        }
        this.orders = orders;
    }


    @Data
    public class Order{
        /** 排序的字段 */
        private String field;
        /** 排序方式（asc升序还是desc降序） */
        private String direction;
    }
}
