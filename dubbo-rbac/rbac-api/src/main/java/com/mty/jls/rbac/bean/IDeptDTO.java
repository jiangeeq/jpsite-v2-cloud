package com.mty.jls.rbac.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @Classname UserDTO
 * @Description 部门Dto
 * @Author 蒋老湿 蒋老湿
 * @Date 2019-04-23 21:26
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel("部门数据传输对象")
public class IDeptDTO implements Serializable {
    @ApiModelProperty("部门id")
    private Integer deptId;

    @ApiModelProperty("部门名称")
    private String name;

    @ApiModelProperty("上级部门")
    private Integer parentId;

    @ApiModelProperty("排序")
    private Integer sort;
}
