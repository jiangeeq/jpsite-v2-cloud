package com.mty.jls.rbac.bean;

import lombok.Data;

import com.dove.jls.common.bean.BaseResponse;
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
public class IPageResponse<T extends Collection> extends BaseResponse<T> implements Serializable {

    public static <T extends Collection> IPageResponse<T> ok() {
        final IPageResponse<T> pageResponse = new IPageResponse<>();
        pageResponse.setCode(BaseResponse.OK);
        return pageResponse;
    }

    public static <T extends Collection> IPageResponse<T> no(String message) {
        final IPageResponse<T> pageResponse = new IPageResponse<>();
        pageResponse.setCode(BaseResponse.BAD_REQUEST);
        pageResponse.setMessage(message);
        return pageResponse;
    }


    private Long page;
    private Long pageSize;
    private Long total;
    private T records;
}
