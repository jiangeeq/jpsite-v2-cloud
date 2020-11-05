package com.dove.jls.common.bean;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Collection;

/**
 * @author jiangpeng
 * @date 2020/11/217:23
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class PageResponse<T extends Collection> extends BaseResponse<T> implements Serializable {

    public static <T extends Collection> PageResponse<T> ok() {
        final PageResponse<T> pageResponse = new PageResponse<>();
        pageResponse.setCode(BaseResponse.OK);
        return pageResponse;
    }

    public static <T extends Collection> PageResponse<T> no(String message) {
        final PageResponse<T> pageResponse = new PageResponse<>();
        pageResponse.setCode(BaseResponse.BAD_REQUEST);
        pageResponse.setMessage(message);
        return pageResponse;
    }


    private Long page;
    private Long pageSize;
    private Long total;
    private T records;
}
