package com.mty.rbac.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Classname RedisVo
 * @Description redisVo
 * @Author Created by 蒋老湿
 * @Date 2019-07-18 16:17
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IRedisVo implements Serializable {

    /**
     * key
     */
    private String key;
    /**
     * value
     */
    private String value;

    /**
     * 过期时间
     */
    private long expireTime;
}
