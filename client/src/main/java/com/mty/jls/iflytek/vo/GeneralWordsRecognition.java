package com.mty.jls.iflytek.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author jiangpeng
 * @date 2020/10/2815:43
 */
@Data
@ApiModel("印刷文字识别")
public class GeneralWordsRecognition {
    @ApiModelProperty("区域块信息")
    private Block[] block;

    @Data
    public static class Block {
        @ApiModelProperty("区域块类型（text-文本，image-图片）")
        private String type;

        @ApiModelProperty("行信息")
        private Line[] line;

    }

    @Data
    public static class Line {
        @ApiModelProperty("后验概率")
        private Float confidence;

        @ApiModelProperty("位置信息")
        private Location location;

        @ApiModelProperty("字（中文），单词（英文）")
        private Word[] word;
    }

    @Data
    public static class Location {
        private LocationDetail top_left;
        private LocationDetail right_bottom;
    }

    @Data
    public static class LocationDetail {
        @ApiModelProperty("对应点的横坐标（像素）")
        private Integer x;

        @ApiModelProperty("对应点的纵坐标（像素）")
        private Integer y;
    }

    @Data
    public static class Word {
        @ApiModelProperty("内容")
        private String content;
        @ApiModelProperty("位置信息")
        private Location location;

    }
}
