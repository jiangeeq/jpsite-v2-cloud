package com.mty.jls.iflytek.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author jiangpeng
 * @date 2020/10/2812:37
 */
@Data
@Accessors(chain = true)
public class LfasrResultVO {
    private List<Lfasr> lfasrList;
}
