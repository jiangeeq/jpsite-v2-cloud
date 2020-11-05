package com.mty.jls.shop.api;

import com.dove.jls.common.bean.PageRequest;
import com.dove.jls.common.bean.PageResponse;
import com.mty.jls.shop.bean.IWhAccountRecordDTO;
import com.mty.jls.shop.bean.IWhAccountRecords;

import java.util.List;

/**
 * <p>
 * 账号充值消费记录 服务类
 * </p>
 *
 * @author 掘金-蒋老湿（公众号：十分钟学编程）
 * @since 2020-10-22
 */
public interface IWhAccountRecordsService {

    Boolean save(IWhAccountRecords accountRecord);

    IWhAccountRecords getById(Integer id);

    PageResponse<List<IWhAccountRecords>> page(PageRequest pageRequest, IWhAccountRecordDTO accountRecordDTO);
}
