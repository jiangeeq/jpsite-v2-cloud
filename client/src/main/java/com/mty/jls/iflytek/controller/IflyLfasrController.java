package com.mty.jls.iflytek.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fasterxml.jackson.databind.JavaType;
import com.mty.jls.contract.model.Response;
import com.mty.jls.dovecommon.utils.FileUtil;
import com.mty.jls.dovecommon.utils.JsonUtil;
import com.mty.jls.dovecommon.utils.ValidateUtil;
import com.mty.jls.iflytek.operation.LfasrSdkOperation;
import com.mty.jls.iflytek.operation.WebOcrOperation;
import com.mty.jls.iflytek.entity.IflyLfasr;
import com.mty.jls.iflytek.service.IflyLfasrService;
import com.mty.jls.iflytek.vo.GeneralWordsRecognition;
import com.mty.jls.iflytek.vo.Lfasr;
import com.mty.jls.iflytek.vo.LfasrResultVO;
import com.mty.jls.upload.service.PicUploadTencentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 掘金-蒋老湿（公众号：十分钟学编程）
 * @since 2020-10-27
 */
@Api(value = "讯飞语音转写服务", tags = "讯飞语音转写服务")
@RestController
@RequestMapping("/iflyLfasr")
@Slf4j
public class IflyLfasrController {
    @Autowired
    private LfasrSdkOperation lfasrSdkOperation;
    @Autowired
    private WebOcrOperation webOcrOperation;
    @Autowired
    private IflyLfasrService iflyLfasrService;
    @Autowired
    private PicUploadTencentService picUploadTencentService;


    @ApiOperation("语音转写-提交任务")
    @PostMapping("/submit")
    public Response uploadFile(@RequestParam("file") MultipartFile uploadFile) throws Exception {
        var hasUploadFile = Objects.nonNull(uploadFile) && uploadFile.getSize() > 0;
        ValidateUtil.validateForResponse(!hasUploadFile, "没有文件");

        boolean isAudio = FileUtil.isAudio(uploadFile.getOriginalFilename());
        ValidateUtil.validateForResponse(!isAudio, "非法的音频格式文件");

//            long videoDuration = FileUtil.getVideoDuration(uploadFile);
//            long totalMinute = videoDuration % 60 == 0 ? videoDuration % 60 : videoDuration % 60 + 1;
//            log.info("[{}]总时长为[{}]分钟", uploadFile.getOriginalFilename(), totalMinute);
        File tempFile = FileUtil.multipartBigFileToFile(uploadFile);

        assert tempFile != null;
        var uploadResult = picUploadTencentService.uploadMax5gFile(tempFile, MediaType.parseMediaType(uploadFile.getContentType()));

        String taskName = "语音转写-" + uploadFile.getOriginalFilename();
        String taskId = lfasrSdkOperation.standard(tempFile.getAbsolutePath(), taskName);

        tempFile.delete();
        return Response.succeed(taskId);
    }

    @ApiOperation("语音转写-获取结果")
    @GetMapping("/result")
    public Response uploadFile(String taskId) {
        String result = lfasrSdkOperation.getResult(taskId);

        JavaType javaType = JsonUtil.buildCollectionType(List.class, Lfasr.class);
        List<Lfasr> lfasrList = JsonUtil.fromJson(result, javaType);
        return Response.succeed(new LfasrResultVO().setLfasrList(lfasrList));
    }

    @ApiOperation("语音转写-获取纯内容结果")
    @GetMapping("/content")
    public Response getContent(String taskId) {
        LambdaQueryWrapper<IflyLfasr> queryWrapper = Wrappers.<IflyLfasr>lambdaQuery().eq(IflyLfasr::getTaskId, taskId);
        IflyLfasr iflyLfasr = iflyLfasrService.getOne(queryWrapper);

        if (Objects.isNull(iflyLfasr) || Strings.isBlank(iflyLfasr.getData())) {
            return Response.fail("没有内容");
        }
        JavaType javaType = JsonUtil.buildCollectionType(List.class, Lfasr.class);
        List<Lfasr> lfasrList = JsonUtil.fromJson(iflyLfasr.getData(), javaType);
        final var content = lfasrList.stream().map(Lfasr::getOnebest).collect(Collectors.joining(","));

        return Response.succeed(content);
    }


    @ApiOperation("语音转写-查看进度")
    @GetMapping("/getProgress")
    public Response getProgress(String taskId) {
        String result = lfasrSdkOperation.getProgress(taskId);
        return Response.succeed(result);
    }


    @ApiOperation("印刷文字识别-提交图片扫描")
    @GetMapping("/submitOcr")
    public Response submitOcr(String filePath) {
        try {
            final var webOcrMessage = webOcrOperation.submit(filePath);
            var dataJson = JsonUtil.encode(webOcrMessage.getData());
            final var wordsRecognition = JsonUtil.decode(dataJson, GeneralWordsRecognition.class);
            return Response.succeed(wordsRecognition);
        } catch (Exception e) {
            log.error(e.getMessage());
            return Response.fail(e.getMessage());
        }
    }
}

