package com.mty.rbac.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
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
@TableName("sys_dept")
@ApiModel("部门实体对象")
public class SysDept extends Model<SysDept> {

    @ApiModelProperty("部门主键ID")
    @TableId(value = "dept_id", type = IdType.AUTO)
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
    @TableField(exist = false)
    private String parentName;

    @ApiModelProperty("非数据库字段-等级")
    @TableField(exist = false)
    private Integer level;

    @ApiModelProperty("非数据库字段-子部门")
    @TableField(exist = false)
    private List<SysDept> children;
}
