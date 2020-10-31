package com.mty.jls.dovecommon.utils;

import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

/**
 * 加解密算法工具测试类
 * @author jiangpeng
 * @date 2020/10/3011:01
 */
public class EncryptUtilTest {

    @Test
    public void testHmacSHA1() {
        String str = EncryptUtil.HmacSHA1("1603987656;1603988256", "g9LG7l9F5dASTDgxyiBQO9espVw63j9K");
        Assert.isTrue("06B33E277CCDC307A5DFA01F33ECAA0D115D4CDE".equalsIgnoreCase(str), "HmacSHA1加密算法不准确");
    }

    @Test
    public void testSHA1() {
        String str = EncryptUtil.SHA1("1603987656;1603988256");
        Assert.isTrue("2a4ec8d155c80a9775e13e0f20d3167b96f46091".equalsIgnoreCase(str), "SHA1加密算法不准确");
    }
}
