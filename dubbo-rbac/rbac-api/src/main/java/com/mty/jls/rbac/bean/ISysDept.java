package com.mty.jls.rbac.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel("部门实体对象")
public class ISysDept {

    @ApiModelProperty("部门主键ID")
    private Integer deptId;

    @ApiModelProperty("部门名称")
    private String name;

    @ApiModelProperty("上级部门")
    private Integer parentId;

    @ApiModelProperty("排序")
    private Integer sort;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("修改时间")
    private LocalDateTime updateTime;

    @ApiModelProperty("是否删除  -1：已删除  0：正常")
    private String delFlag;

    @ApiModelProperty("非数据库字段-上级部门")
    private String parentName;

    @ApiModelProperty("非数据库字段-等级")
    private Integer level;

    @ApiModelProperty("非数据库字段-子部门")
    private List<ISysDept> children;
}
