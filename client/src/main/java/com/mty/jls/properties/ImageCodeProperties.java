package com.mty.jls.properties;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 图片验证码配置项
 * @author 掘金-蒋老湿（公众号：十分钟学编程）
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ImageCodeProperties extends SmsCodeProperties {
    /**
     * 图片宽
     */
    private int width = 67;
    /**
     * 图片高
     */
    private int height = 23;

    public ImageCodeProperties() {
        super.setLength(4);
    }
}
