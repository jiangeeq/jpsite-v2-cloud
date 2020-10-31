package com.mty.jls.upload.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import org.springframework.http.MediaType;
import com.mty.jls.dovecommon.utils.DateUtil;
import com.mty.jls.dovecommon.utils.EncryptUtil;
import com.mty.jls.dovecommon.utils.FileUtil;
import com.mty.jls.dovecommon.utils.RestTemplateUtils;
import com.mty.jls.dovecommon.utils.ValidateUtil;
import com.mty.jls.upload.vo.UploadResult;
import com.mty.jls.upload.config.TencentConfig;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author jiangpeng
 * @date 2019/11/159:52
 */
@Service
@Slf4j
public class PicUploadTencentService {

    @Autowired
    private COSClient ossTencentClient;
    @Autowired
    private TencentConfig tencentConfig;

    private ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 可以将本地不超过5GB的对象（Object）以网页表单（HTML Form）的形式上传至指定存储桶中。
     * 该 API 的请求者需要对存储桶有写入权限
     * 参考 https://cloud.tencent.com/document/api/436/14690
     *
     * @param file
     */
    public String uploadMax5gFile(File file, MediaType mediaType) throws Exception {
        final HashMap<Object, Object> hashMap = Maps.newHashMap();

        // 获取当前时间对应的 Unix 时间戳(单位秒)
        long startTimestamp = System.currentTimeMillis() / 1000L;
        // 有效时长（单位秒）
        long effectiveTime = 600;
        // 根据上述时间戳和期望的签名有效时长算出签名过期时间对应的 Unix 时间戳 EndTimestamp
        long endTimestamp = startTimestamp + effectiveTime;
        // 格式为StartTimestamp;EndTimestamp
        final var keyTime = startTimestamp + ";" + endTimestamp;
        // 该策略的过期时间，ISO8601 格式字符串
        final var expiration = DateUtil.timeStamp2UTC(endTimestamp);
        var policy = String.format("{\"expiration\":\"%s\",\"conditions\":[{\"bucket\":\"%s\"},{\"q-sign-algorithm\":\"sha1\"},{\"q-ak\":\"%s\"}," +
                "{\"q-sign-time\":\"%s\"}]}",
                        expiration, tencentConfig.getBucketName(), tencentConfig.getSecretId(), keyTime);

        String stringToSign = EncryptUtil.SHA1(policy).toLowerCase();
        String signKey = EncryptUtil.HmacSHA1(keyTime, tencentConfig.getSecretKey()).toLowerCase();
        String signature = EncryptUtil.HmacSHA1(stringToSign, signKey).toLowerCase();

        log.debug("过程参数：SignKey=[{}], StringToSign=[{}], Signature=[{}]", signKey, stringToSign, signature);
        log.debug("policy=[{}]", policy);

        // 对象键，可在对象键中指定${filename}通配符，此时将使用实际上传的文件的文件名替换对象键中的通配符
        hashMap.put("key", createFilePath(file.getName()));
        // 经过 Base64 编码的“策略”（Policy）内容
        hashMap.put("policy", EncryptUtil.Base64Encode(policy));
        // 签名哈希算法，固定为 sha1
        hashMap.put("q-sign-algorithm", "sha1");
        // 上文所述的 SecretId
        hashMap.put("q-ak", tencentConfig.getSecretId());
        // 上文所生成的 KeyTime
        hashMap.put("q-key-time", keyTime);
        // 上文所生成的 Signature
        hashMap.put("q-signature", signature);
        // file 字段必须放在整个表单的最后面。
        hashMap.put("file", file);
        String url = "https://haoke-1259745054.cos.ap-guangzhou.myqcloud.com/";
        String result = RestTemplateUtils.executeMoreMultipartFormData(url, hashMap, mediaType);
        return result;
    }

    /**
     * 适用于图片类小文件上传（20MB以下）
     * @param uploadFile
     * @return
     */
    public UploadResult upload(File uploadFile) {
        UploadResult uploadResult = new UploadResult();

        // 文件新路径
        String filePath = createFilePath(uploadFile.getName());
        // 上传文件
        try {
            PutObjectRequest putObjectRequest = new PutObjectRequest(tencentConfig.getBucketName(), filePath,
                    uploadFile);

            PutObjectResult putObjectResult = ossTencentClient.putObject(putObjectRequest);
            uploadResult.setResponse(objectMapper.writeValueAsString(putObjectResult));
        } catch (Exception e) {
            e.printStackTrace();
            uploadResult.setStatus("error");
            return uploadResult;
        }

        uploadResult.setStatus("done");
        uploadResult.setName(tencentConfig.getUrlPrefix() + filePath);
        uploadResult.setUid(String.valueOf(System.currentTimeMillis()));

        return uploadResult;
    }

    private String createFilePath(String fileName) {
        AtomicReference<String> prefix = new AtomicReference<>("other/");
        ValidateUtil.validateExecute(FileUtil.isAudio(fileName), x -> prefix.set("audio/"));
        ValidateUtil.validateExecute(FileUtil.isLegalPic(fileName), x -> prefix.set("image/"));

        LocalDate dateTime = LocalDate.now();
        return prefix.get() + dateTime.getYear()
                + "/" + dateTime.getMonthValue() + "/"
                + dateTime.getDayOfMonth() + "/" + System.currentTimeMillis() + "-" +
                RandomUtils.nextInt(100, 9999) + "-" + fileName;
    }
}
