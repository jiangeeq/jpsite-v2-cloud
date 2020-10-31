package com.mty.jls.upload.service;

import com.mty.jls.upload.vo.UploadResult;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

/**
 * @author jiangpeng
 * @date 2019/11/1510:43
 */
@Service
public class PicUploadFileSystemService {

    public UploadResult upload(MultipartFile uploadFile) {
        UploadResult uploadResult = new UploadResult();

        String fileName = uploadFile.getOriginalFilename();
        String filePath = createFilePath(fileName);

        File newFile = new File(filePath);
        try {
            uploadFile.transferTo(newFile);
        } catch (IOException e) {
            e.printStackTrace();
            uploadResult.setStatus("error");
            return uploadResult;
        }

        uploadResult.setStatus("done");
        uploadResult.setName(newFile.getAbsolutePath());
        uploadResult.setUid(String.valueOf(System.currentTimeMillis()));

        return uploadResult;
    }

    private String createFilePath(String sourceFileName) {
        LocalDate dateTime = LocalDate.now();

        String fileFolder = "D:\\" + File.separator + "images" + File.separator + dateTime.getYear()
                + File.separator + dateTime.getMonthValue() + File.separator
                + dateTime.getDayOfMonth();

        File file = new File(fileFolder);
        // 如果目录不存在，则创建目录
        if (!file.isDirectory()) {
            file.mkdirs();
        }
        // 生成新的文件名
        String fileName = System.currentTimeMillis() +
                RandomUtils.nextInt(100, 9999) + "." +
                StringUtils.substringAfterLast(sourceFileName, ".");

        return fileFolder + File.separator + fileName;
    }

}
