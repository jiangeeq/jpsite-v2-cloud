package com.mty.jls.rbac.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.mty.jls.rbac.domain.SysDict;
import com.mty.jls.rbac.dto.DictDTO;
import com.mty.jls.rbac.mapper.SysDictMapper;
import com.mty.jls.rbac.service.ISysDictService;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class SysDictServiceImpl extends ServiceImpl<SysDictMapper, SysDict> implements ISysDictService {

    @Override
    public IPage<SysDict> selectDictList(int page, int pageSize) {
        Page<SysDict> dictPage = new Page<>(page, pageSize);
        IPage<SysDict> sysDictPage = baseMapper.selectPage(dictPage, Wrappers.<SysDict>lambdaQuery().select(SysDict::getId, SysDict::getName,
                SysDict::getDescription, SysDict::getRemark, SysDict::getCreateTime));
        // 过滤掉描述为空的数据
        sysDictPage.setRecords(sysDictPage.getRecords().stream().filter(sysDict -> Strings.isNotEmpty(sysDict.getDescription())).collect(Collectors.toList()));
        return sysDictPage;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateDict(DictDTO dictDto) {
        SysDict sysDict = new SysDict();
        BeanUtil.copyProperties(dictDto, sysDict);
        return baseMapper.updateById(sysDict) > 0;
    }

    @Override
    public List<SysDict> selectDictDetailList(String name) {
        return baseMapper.selectList(Wrappers.<SysDict>lambdaQuery().select(SysDict::getId, SysDict::getName, SysDict::getValue, SysDict::getLabel,
                SysDict::getSort).eq(SysDict::getName, name)).stream().filter(sysDict -> Strings.isNotEmpty(sysDict.getValue())).collect(Collectors.toList());

    }

    @Override
    public boolean deleteDictByName(String name) {
        return baseMapper.delete(Wrappers.<SysDict>lambdaQuery().eq(SysDict::getName, name)) > 0;
    }

    @Override
    public boolean removeById(Serializable id) {
        return super.removeById(id);
    }
}
