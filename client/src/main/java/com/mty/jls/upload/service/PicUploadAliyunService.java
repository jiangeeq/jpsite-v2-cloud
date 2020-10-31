package com.mty.jls.upload.service;

import com.aliyun.oss.OSS;
import com.aliyun.oss.model.PutObjectResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mty.jls.dovecommon.utils.FileUtil;
import com.mty.jls.dovecommon.utils.ValidateUtil;
import com.mty.jls.upload.vo.UploadResult;
import com.mty.jls.upload.config.AliyunConfig;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author jiangpeng
 * @date 2019/11/159:52
 */
@Service
public class PicUploadAliyunService {

    @Autowired
    private OSS ossAliyunClient;
    @Autowired
    private AliyunConfig aliyunConfig;

    private ObjectMapper objectMapper = new ObjectMapper();

    public UploadResult upload(MultipartFile uploadFile) {

        UploadResult uploadResult = new UploadResult();

        // 文件新路径
        String filePath = createFilePath(uploadFile);
        // 上传文件
        try {
            PutObjectResult putObjectResult = ossAliyunClient.putObject(aliyunConfig.getBucketName(), filePath,
                    new ByteArrayInputStream(uploadFile.getBytes()));
            uploadResult.setResponse(objectMapper.writeValueAsString(putObjectResult));
        } catch (Exception e) {
            e.printStackTrace();
            uploadResult.setStatus("error");
            return uploadResult;
        }

        uploadResult.setStatus("done");
        uploadResult.setName(aliyunConfig.getUrlPrefix() + filePath);
        uploadResult.setUid(String.valueOf(System.currentTimeMillis()));

        return uploadResult;
    }

    private String createFilePath(MultipartFile uploadFile) {
        String fileName = uploadFile.getOriginalFilename();
        AtomicReference<String> prefix = new AtomicReference<>("other/");
        ValidateUtil.validateExecute(FileUtil.isAudio(fileName), x -> prefix.set("audio/"));
        ValidateUtil.validateExecute(FileUtil.isLegalPic(fileName), x -> prefix.set("image/"));

        LocalDate dateTime = LocalDate.now();
        return prefix.get() + dateTime.getYear()
                + "/" + dateTime.getMonthValue() + "/"
                + dateTime.getDayOfMonth() + "/" + System.currentTimeMillis() +
                RandomUtils.nextInt(100, 9999) + "." +
                StringUtils.substringAfterLast(fileName, ".");
    }
}
