package com.mty.jls.iflytek.controller;

import com.mty.jls.contract.model.Response;
import com.dove.jls.common.utils.FileUtil;
import com.mty.jls.upload.service.PicUploadTencentService;
import com.mty.jls.upload.vo.UploadResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * @author jiangpeng
 * @date 2020/10/3014:20
 */
@Api(value = "文件操作管理", tags = "文件操作管理")
@RestController("/file")
public class FileController {
    @Autowired
    private PicUploadTencentService picUploadTencentService;

    @ApiOperation("上传文件到腾讯云-不超过5G")
    @PostMapping("/upload5gTx")
    public Response upload5gTx(@RequestParam("file") MultipartFile file) throws Exception {
        final File tempFile = FileUtil.multipartBigFileToFile(file);
        String result = picUploadTencentService.uploadMax5gFile(tempFile, MediaType.parseMediaType(file.getContentType()));
        tempFile.delete();
        return Response.succeed(result);
    }

    @ApiOperation("上传文件到腾讯云-不超过20M")
    @PostMapping("/upload20mTx")
    public Response upload20mTx(@RequestParam("file") MultipartFile file) throws Exception {
        final File tempFile = FileUtil.multipartFileToFile(file);
        UploadResult result = picUploadTencentService.upload(tempFile);
        return Response.succeed(result);
    }
}
