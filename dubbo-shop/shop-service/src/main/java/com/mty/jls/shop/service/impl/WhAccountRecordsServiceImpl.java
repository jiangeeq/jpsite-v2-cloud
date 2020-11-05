package com.mty.jls.shop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.dove.jls.common.bean.PageRequest;
import com.dove.jls.common.bean.PageResponse;
import com.dove.jls.common.utils.BeanPlusUtil;
import com.mty.jls.shop.api.IWhAccountRecordsService;
import com.mty.jls.shop.bean.IWhAccountRecordDTO;
import com.mty.jls.shop.bean.IWhAccountRecords;
import com.mty.jls.shop.dao.WhAccountRecordsDao;
import com.mty.jls.shop.entity.WhAccountRecords;
import org.apache.dubbo.config.annotation.Service;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 账号充值消费记录 服务实现类
 * </p>
 *
 * @author 掘金-蒋老湿（公众号：十分钟学编程）
 * @since 2020-10-22
 */
@Service(cluster = "failsafe",
        loadbalance = "roundrobin",
        version = "1.0.0"
)
public class WhAccountRecordsServiceImpl extends ServiceImpl<WhAccountRecordsDao, WhAccountRecords> implements IWhAccountRecordsService {

    @Override
    public Boolean save(IWhAccountRecords accountRecord) {
        return baseMapper.insert(BeanPlusUtil.copySingleProperties(accountRecord, WhAccountRecords::new)) > 0;
    }

    @Override
    public IWhAccountRecords getById(Integer id) {
        final WhAccountRecords whAccountRecords = baseMapper.selectById(id);
        return BeanPlusUtil.copySingleProperties(whAccountRecords, IWhAccountRecords::new);
    }

    @Override
    public PageResponse<List<IWhAccountRecords>> page(PageRequest pageRequest, IWhAccountRecordDTO accountRecordDTO) {
        final LambdaQueryWrapper<WhAccountRecords> queryWrapper = Wrappers.<WhAccountRecords>lambdaQuery().between(WhAccountRecords::getCreateTime,
                accountRecordDTO.getMinCreateTime(), accountRecordDTO.getMaxCreateTime())
                .or()
                .between(WhAccountRecords::getAmount, accountRecordDTO.getMinAmount(), accountRecordDTO.getMaxAmount())
                .or()
                .eq(WhAccountRecords::getType, accountRecordDTO.getType());

        final Page<WhAccountRecords> page = new Page<>(pageRequest.getPageNumber(), pageRequest.getPageSize());
        final Page<WhAccountRecords> recordsPage = baseMapper.selectPage(page, queryWrapper);

        final PageResponse<List<IWhAccountRecords>> pageResponse = PageResponse.ok();
        pageResponse.setPage(recordsPage.getCurrent()).setPageSize(recordsPage.getSize()).setTotal(recordsPage.getTotal())
                .setRecords(BeanPlusUtil.copyListProperties(recordsPage.getRecords(), IWhAccountRecords::new));
        return pageResponse;
    }

}
