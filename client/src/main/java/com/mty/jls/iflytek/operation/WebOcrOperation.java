package com.mty.jls.iflytek.operation;

import com.dove.jls.common.utils.FileUtil;
import com.dove.jls.common.utils.HttpUtil;
import com.dove.jls.common.utils.JsonUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * @author jiangpeng
 * @date 2020/10/2815:14
 */
@Component
@Slf4j
public class WebOcrOperation {
    // OCR webapi 接口地址
    private static final String WEBOCR_URL = "http://webapi.xfyun.cn/v1/service/v1/ocr/general";
    // 应用ID (必须为webapi类型应用，并印刷文字识别服务，参考帖子如何创建一个webapi应用：http://bbs.xfyun.cn/forum.php?mod=viewthread&tid=36481)
    private static final String APPID = "5c0751e2";
    // 接口密钥(webapi类型应用开通印刷文字识别服务后，控制台--我的应用---印刷文字识别---服务的apikey)
    private static final String API_KEY = "e38f5510435bbeea8b635cec93e35307";
    // 是否返回位置信息
    private static final String LOCATION = "false";
    // 语种(可选值：en（英文），cn|en（中文或中英混合)
    private static final String LANGUAGE = "cn|en";

    /**
     * OCR WebAPI 调用示例程序
     *
     * @param picPath
     * @throws IOException
     */
    public WebOcrMessage submit(String picPath) throws IOException {
        Map<String, String> header = buildHttpHeader();
        byte[] imageByteArray = FileUtil.read(picPath);
        String imageBase64 = new String(Base64.encodeBase64(imageByteArray), "UTF-8");
        final var reqParam = Map.of("image", URLEncoder.encode(imageBase64, "UTF-8"));
//        final var result = RestTemplateUtils.executeHttpPostFormData(WEBOCR_URL, header, reqParam, String.class);
        String result = HttpUtil.doPost1(WEBOCR_URL, header, "image=" + URLEncoder.encode(imageBase64, "UTF-8"));

        log.info("OCR WebAPI 接口调用结果：[{}]", result);
        //  错误码链接：https://www.xfyun.cn/document/error-code (code返回错误码时必看)

        return JsonUtil.decode(result, WebOcrMessage.class);
    }

    /**
     * 组装http请求头
     */
    private Map<String, String> buildHttpHeader() throws UnsupportedEncodingException {
        String curTime = System.currentTimeMillis() / 1000L + "";
        String param = "{\"location\":\"" + LOCATION + "\",\"language\":\"" + LANGUAGE + "\"}";
        String paramBase64 = new String(Base64.encodeBase64(param.getBytes("UTF-8")));
        String checkSum = DigestUtils.md5Hex(API_KEY + curTime + paramBase64);
        Map<String, String> header = new HashMap<String, String>();
        header.put("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
        header.put("X-Param", paramBase64);
        header.put("X-CurTime", curTime);
        header.put("X-CheckSum", checkSum);
        header.put("X-Appid", APPID);
        return header;
    }

    @ApiModel("接口返回参数")
    @Data
    public static class WebOcrMessage {
        @ApiModelProperty("结果码")
        private String code;
        @ApiModelProperty("详见data说明")
        private Object data;
        @ApiModelProperty("描述")
        private String desc;
        @ApiModelProperty("会话id")
        private String sid;
    }
}
