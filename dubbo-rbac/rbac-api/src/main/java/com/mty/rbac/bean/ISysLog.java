package com.mty.rbac.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

@ApiModel("系统日志")
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ISysLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    private Integer id;

    @ApiModelProperty("操作IP")
    private String requestIp;

    @ApiModelProperty("操作类型 1 操作记录 2异常记录")
    private Integer type;

    @ApiModelProperty("操作人ID")
    private String userName;

    @ApiModelProperty("操作描述")
    private String description;

    @ApiModelProperty("请求方法")
    private String actionMethod;

    @ApiModelProperty("请求url")
    private String actionUrl;

    @ApiModelProperty("请求参数")
    private String params;

    @ApiModelProperty("浏览器")
    private String ua;

    @ApiModelProperty("类路径")
    private String classPath;

    @ApiModelProperty("请求方法")
    private String requestMethod;

    @ApiModelProperty("开始时间")
    private LocalDateTime startTime;

    @ApiModelProperty("完成时间")
    private LocalDateTime finishTime;

    @ApiModelProperty("消耗")
    private Long consumingTime;

    @ApiModelProperty("异常详情信息 堆栈信息")
    private String exDetail;

    @ApiModelProperty("异常描述 e.getMessage")
    private String exDesc;


}
