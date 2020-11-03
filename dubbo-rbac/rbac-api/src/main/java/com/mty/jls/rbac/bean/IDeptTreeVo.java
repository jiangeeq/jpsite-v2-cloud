package com.mty.jls.rbac.bean;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * @Classname DeptTreeVo
 * @Description 构建部门树vo
 * @Author Created by 蒋老湿
 * @Date 2019-06-09 15:15
 * @Version 1.0
 */
@Setter
@Getter
@ToString
@Accessors(chain = true)
public class IDeptTreeVo implements Serializable {

    private int id;
    private String label;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<IDeptTreeVo> children;

}
