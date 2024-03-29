package com.mty.jls.rbac.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dove.jls.common.utils.BeanPlusUtil;
import com.mty.jls.rbac.api.ISysDeptService;
import com.mty.jls.rbac.bean.IDeptDTO;
import com.mty.jls.rbac.bean.IDeptTreeVo;
import com.mty.jls.rbac.bean.ISysDept;
import com.mty.jls.rbac.domain.SysDept;
import com.mty.jls.rbac.mapper.SysDeptMapper;
import com.mty.jls.rbac.util.RbacUtil;
import com.mty.jls.rbac.vo.DeptTreeVo;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


@Service(cluster = "failsafe",
        loadbalance = "roundrobin",
        group = "rbac",
        version = "1.0.0"
)
public class SysDeptServiceImpl extends ServiceImpl<SysDeptMapper, SysDept> implements ISysDeptService {

    @Override
    public List<ISysDept> selectDeptList() {
        List<SysDept> allDeptList = baseMapper.selectList(Wrappers.<SysDept>lambdaQuery().select(SysDept::getDeptId, SysDept::getName,
                SysDept::getParentId, SysDept::getSort, SysDept::getCreateTime));
        List<SysDept> parentDeptList = allDeptList.stream()
                .filter(sysDept -> sysDept.getParentId() == 0 || ObjectUtil.isNull(sysDept.getParentId()))
                .peek(sysDept -> sysDept.setLevel(0))
                .collect(Collectors.toList());
        RbacUtil.findChildrenToDo(parentDeptList, allDeptList);
        final List<ISysDept> iSysDepts = BeanPlusUtil.copyListProperties(parentDeptList, ISysDept::new);
        return iSysDepts;
    }


    @Override
    public boolean updateDeptById(IDeptDTO entity) {
        SysDept sysDept = BeanPlusUtil.copySingleProperties(entity, SysDept::new);
        sysDept.setUpdateTime(LocalDateTime.now());
        return this.updateById(sysDept);
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean removeById(Serializable id) {
        // 部门层级删除
        List<Integer> idList =
                this.list(Wrappers.<SysDept>query().lambda().eq(SysDept::getParentId, id)).stream().map(SysDept::getDeptId).collect(Collectors.toList());
        // 删除自己
        idList.add((Integer) id);
        return super.removeByIds(idList);
    }

    @Override
    public String selectDeptNameByDeptId(int deptId) {
        return baseMapper.selectOne(Wrappers.<SysDept>query().lambda().select(SysDept::getName).eq(SysDept::getDeptId, deptId)).getName();
    }

    /**
     * 获取这个部门下所拥有的所有部门
     *
     * @param deptId
     * @return 当前部门id与下级部门id
     */
    @Override
    public List<Integer> selectDeptIds(int deptId) {
        SysDept department = this.getDepartment(deptId);
        List<Integer> deptIdList = new ArrayList<>();
        if (department != null) {
            deptIdList.add(department.getDeptId());
            resolveChildrenDeptIdList(deptIdList, department);
        }
        return deptIdList;
    }

    @Override
    public List<IDeptTreeVo> getDeptTree() {
        List<SysDept> allDeptList = baseMapper.selectList(Wrappers.<SysDept>query().select("dept_id", "name", "parent_id", "sort", "create_time"));
        List<DeptTreeVo> parentDeptVoList =
                allDeptList.stream().filter(sysDept -> sysDept.getParentId() == 0 || ObjectUtil.isNull(sysDept.getParentId()))
                        .map(sysDept -> {
                            DeptTreeVo deptTreeVo = new DeptTreeVo();
                            deptTreeVo.setId(sysDept.getDeptId());
                            deptTreeVo.setLabel(sysDept.getName());
                            return deptTreeVo;
                        }).collect(Collectors.toList());

        RbacUtil.findChildrenToVo(parentDeptVoList, allDeptList);
        ArrayList<IDeptTreeVo> iDeptTreeVos = new ArrayList<>();
        BeanUtil.copyProperties(parentDeptVoList, iDeptTreeVos);
        return iDeptTreeVos;
    }

    @Override
    public Boolean save(ISysDept sysDept) {
        return baseMapper.insert(BeanPlusUtil.copySingleProperties(sysDept, SysDept::new)) > 0;
    }


    /**
     * 根据部门ID获取该部门及其下属部门树
     * todo 可以使用redis优化
     */
    private SysDept getDepartment(Integer deptId) {
        List<SysDept> allDeptList = baseMapper.selectList(Wrappers.<SysDept>query().select("dept_id", "name", "parent_id", "sort",
                "create_time"));
        Map<Integer, SysDept> deptMap = allDeptList.stream().collect(
                Collectors.toMap(SysDept::getDeptId, department -> department));

        for (SysDept dept : deptMap.values()) {
            // 判断当前部门是否有上级部门
            SysDept parentDept = deptMap.get(dept.getParentId());
            if (Objects.nonNull(parentDept)) {
                // 把当前部门追加到上级部门的children集合中
                List<SysDept> children = parentDept.getChildren() == null ? new ArrayList<>() : parentDept.getChildren();
                children.add(dept);
                parentDept.setChildren(children);
            }
        }
        return deptMap.get(deptId);
    }

    /**
     * 递归遍历取出所有的部门id
     *
     * @param deptIdList
     * @param department
     */
    private void resolveChildrenDeptIdList(List<Integer> deptIdList, SysDept department) {
        List<SysDept> children = department.getChildren();
        if (children != null) {
            for (SysDept d : children) {
                deptIdList.add(d.getDeptId());
                resolveChildrenDeptIdList(deptIdList, d);
            }
        }
    }


}
