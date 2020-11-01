package com.mty.rbac.constant;

/**
 * @author jiangpeng
 * @date 2020/10/1317:25
 */
public class PropertiesConstant {
    // 是否前后端分离
    public static final String IS_FRONT_BACKEND_SEPARATION = "is.front.backend.separation";

    public static final String IS_ENABLE_OAUTH2_SSO =  "is.enable.oauth2.sso";
    // 是否启用验证码校验
    public static final String IS_ENABLE_LOGIN_VERIFY_CODE = "is.enable.login.verify.code";
    // 是否启用JWT登录模式
    public static final String IS_ENABLE_LOGIN_JWT = "is.enable.login.jwt";
    // jwt签名秘钥
    public static final String JWT_SIGN_KEY = "jwt.sign.key";
    // 钉钉群机器人签名
    public static final String OAPI_DINGTALK_SECRET = "oapi.dingtalk.secret";
    // 钉钉web hook url地址
    public static final String OAPI_DINGTALK_URL = "oapi.dingtalk.url";
}
