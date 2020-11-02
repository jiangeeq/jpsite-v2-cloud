package com.mty.jls.rbac.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mty.jls.rbac.api.ISysDictService;
import com.mty.jls.rbac.mapper.SysDictMapper;
import com.mty.jls.rbac.bean.IDictDTO;
import com.mty.jls.rbac.bean.ISysDict;
import com.mty.jls.rbac.domain.SysDict;
import org.apache.dubbo.config.annotation.Service;
import org.apache.logging.log4j.util.Strings;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service(cluster = "failsafe",
        loadbalance = "roundrobin",
        version = "1.0.0"
)
public class SysDictServiceImpl extends ServiceImpl<SysDictMapper, SysDict> implements ISysDictService {

   /* @Override
    public IPage<SysDict> selectDictList(int page, int pageSize) {
        Page<SysDict> dictPage = new Page<>(page, pageSize);
        IPage<SysDict> sysDictPage = baseMapper.selectPage(dictPage, Wrappers.<SysDict>lambdaQuery().select(SysDict::getId, SysDict::getName,
                SysDict::getDescription, SysDict::getRemark, SysDict::getCreateTime));
        // 过滤掉描述为空的数据
        sysDictPage.setRecords(sysDictPage.getRecords().stream().filter(sysDict -> Strings.isNotEmpty(sysDict.getDescription())).collect(Collectors.toList()));
        return sysDictPage;
    }*/

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean updateDict(IDictDTO dictDto) {
        SysDict sysDict = new SysDict();
        BeanUtil.copyProperties(dictDto, sysDict);
        return baseMapper.updateById(sysDict) > 0;
    }

    @Override
    public List<ISysDict> selectDictDetailList(String name) {
        var list = baseMapper.selectList(Wrappers.<SysDict>lambdaQuery().select(SysDict::getId, SysDict::getName, SysDict::getValue, SysDict::getLabel,
                SysDict::getSort).eq(SysDict::getName, name)).stream().filter(sysDict -> Strings.isNotEmpty(sysDict.getValue())).collect(Collectors.toList());
        ArrayList<ISysDict> iSysDicts = new ArrayList<>();
        BeanUtil.copyProperties(list, iSysDicts);
        return iSysDicts;
    }

    @Override
    public Boolean deleteDictByName(String name) {
        return baseMapper.delete(Wrappers.<SysDict>lambdaQuery().eq(SysDict::getName, name)) > 0;
    }

    @Override
    public Boolean removeById(Integer id) {
        return baseMapper.deleteById(id) > 0;
    }
}
