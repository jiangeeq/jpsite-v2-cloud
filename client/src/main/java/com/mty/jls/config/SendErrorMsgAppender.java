package com.mty.jls.config;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Layout;
import ch.qos.logback.core.UnsynchronizedAppenderBase;
import com.mty.jls.contract.constant.PropertiesConstant;
import com.mty.jls.dovecommon.utils.RestTemplateUtils;
import com.mty.jls.dovecommon.utils.SpringUtil;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.InetAddress;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.Base64;
import java.util.Map;

/**
 * UnsynchronizedAppenderBase 用于异步处理，不阻塞主线程
 * 拦截error日志，并发送到钉钉群
 * @author jiangpeng
 * @date 2020/10/3115:38
 */
@Getter
@Setter
public class SendErrorMsgAppender extends UnsynchronizedAppenderBase<ILoggingEvent> {

    Layout<ILoggingEvent> layout;

    //自定义配置
    String printString;

    @Override
    public void start() {
        //这里可以做些初始化判断 比如layout不能为null ,
        if (layout == null) {
            addWarn("Layout was not defined");
        }
        //或者写入数据库 或者redis时 初始化连接等等
        super.start();
    }

    @Override
    public void stop() {
        //释放相关资源，如数据库连接，redis线程池等等
        if (!isStarted()) {
            return;
        }
        super.stop();
    }

    @Override
    public void append(ILoggingEvent event) {
        if (event.getLevel() == Level.ERROR) {
            try {
                //获取服务器Ip，告知哪台服务器抛异常
                String ip = InetAddress.getLocalHost().getHostAddress();
                String message = ip + "  " + layout.doLayout(event);
                sendMsgToDingDing(message);
            } catch (UnknownHostException ignored) {
                addWarn("获取服务器ip失败");
            }
        }
    }

    private void sendMsgToDingDing(String msg) {
        Text text = new Text();
        text.setContent(msg);
        DdMsgBody msgBody = DdMsgBody.builder().msgtype("text").text(text).build();

        Long timestamp = System.currentTimeMillis();
        String secret = SpringUtil.environment(PropertiesConstant.OAPI_DINGTALK_SECRET);
        var charsetName = "UTF-8";
        // 把timestamp+"\n"+密钥当做签名字符串
        String stringToSign = timestamp + "\n" + secret;
        try {
            // 使用HmacSHA256算法计算签名
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secret.getBytes(charsetName), "HmacSHA256"));
            byte[] signData = mac.doFinal(stringToSign.getBytes());
            // 最后再把签名参数再进行urlEncode，得到最终的签名（需要使用UTF-8字符集）
            String sign = URLEncoder.encode(Base64.getEncoder().encodeToString(signData), charsetName);
            var paramMap = Map.of("timestamp", timestamp, "sign", sign);
            var sendUrl = RestTemplateUtils.buildGetUrlByMap(SpringUtil.environment(PropertiesConstant.OAPI_DINGTALK_URL), paramMap);
            RestTemplateUtils.executeHttpPost(sendUrl, msgBody);
        } catch (Exception e) {
        }
    }

    @Builder
    @Data
    private static class DdMsgBody {
        private String msgtype;
        private Text text;
        private Markdown markdown;
    }

    @Accessors(chain = true)
    @Data
    private class Markdown {
        private String title;
    }

    @Accessors(chain = true)
    @Data
    private class Text {
        private String content;
    }
}
