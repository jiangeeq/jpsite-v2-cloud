package com.mty.jls.iflytek.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author jiangpeng
 * @date 2020/10/2812:14
 */
@ApiModel("语音转写结果")
@Data
public class Lfasr {
    @ApiModelProperty("句子相对于本音频的起始时间，单位为ms")
    private String bg;

    @ApiModelProperty("句子相对于本音频的终止时间，单位为ms")
    private String ed;

    @ApiModelProperty("句子内容")
    private String onebest;

    @ApiModelProperty("说话人编号，从1开始，未开启说话人分离时speaker都为0")
    private String speaker;

    @ApiModelProperty("句子标识，相同si表示同一句话，从0开始，注：仅开启分词或者多候选时返回")
    private String si;

    @ApiModelProperty("分词列表 注：仅开启分词或者多候选时返回")
    private String wordsResultList;

    @ApiModelProperty("多候选列表，按置信度排名 注：仅开启分词或者多候选时返回")
    private String alternativeList;

    @ApiModelProperty("词相对于本句子的起始帧，其中一帧是10ms 注：仅开启分词或者多候选时返回")
    private String wordBg;

    @ApiModelProperty("词相对于本句子的终止帧，其中一帧是10ms 注：仅开启分词或者多候选时返回")
    private String wordEd;

    @ApiModelProperty("词内容 注：仅开启分词或者多候选时返回")
    private String wordsName;

    @ApiModelProperty("句子置信度，范围为[0,1] 注：仅开启分词或者多候选时返回")
    private String wc;

    @ApiModelProperty("词属性，n代表普通词，r代表人名，d代表数字，m代表量词，s代表顺滑词（语气词），t代表地名&多音字，p代表标点，g代表分段标识 注：仅开启分词或者多候选时返回")
    private String wp;
}
