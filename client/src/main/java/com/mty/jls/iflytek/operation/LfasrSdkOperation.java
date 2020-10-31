package com.mty.jls.iflytek.operation;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.iflytek.msp.lfasr.LfasrClient;
import com.iflytek.msp.lfasr.model.Message;
import com.mty.jls.dovecommon.exception.BusinessException;
import com.mty.jls.dovecommon.utils.JsonUtil;
import com.mty.jls.dovecommon.utils.ValidateUtil;
import com.mty.jls.iflytek.entity.IflyLfasr;
import com.mty.jls.iflytek.enums.LfasrStatuEnum;
import com.mty.jls.iflytek.service.IflyLfasrService;
import com.mty.jls.utils.RbacUtil;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.Objects;

/**
 * <p>Title : SDK 调用实例</p>
 * <p>Description : </p>
 * <p>Date : 2020/4/20 </p>
 *
 * @author : hejie
 */
@Component
@Slf4j
public class LfasrSdkOperation {
    @Autowired
    private IflyLfasrService iflyLfasrService;

    @Value("${iflytek.lfasr.appId}")
    private String appId;
    @Value("${iflytek.lfasr.secretKey}")
    private String secretKey;

    LfasrClient lfasrClient = null;

    @Bean
    public LfasrClient lfasrClient() {
        this.lfasrClient = LfasrClient.getInstance(appId, secretKey);
        return lfasrClient;
    }


    /**
     * 提交语音转写任务
     *
     * @throws InterruptedException e
     */
    public String standard(String filePath, String taskName) {

        Message task = lfasrClient.upload(filePath);
        String taskId = task.getData();
        log.info("转写任务 taskId: [{}]", taskId);

        IflyLfasr iflyLfasr = new IflyLfasr().setTaskId(taskId).setTaskName(taskName)
                .setCreator(RbacUtil.getSecurityUser().getUserId()).setStatus(0);
        iflyLfasrService.save(iflyLfasr);

        return taskId;
    }

    /**
     * 查看转写进度
     *
     * @param taskId
     * @return
     */
    public String getProgress(String taskId) throws BusinessException {
        LambdaQueryWrapper<IflyLfasr> queryWrapper = Wrappers.<IflyLfasr>lambdaQuery().eq(IflyLfasr::getTaskId, taskId);
        IflyLfasr iflyLfasr = iflyLfasrService.getOne(queryWrapper);

        ValidateUtil.validateForResponse(Objects.isNull(iflyLfasr), "%s语音转写任务不存在", taskId);
        ValidateUtil.validateForResponse(iflyLfasr.getStatus().equals(LfasrStatuEnum.REVERING_RESULT_UPLOAD.getCode()), "%s语音转写任务已完成", taskId);

        Message message = lfasrClient.getProgress(taskId);
        Map<String, String> stringMap = JsonUtil.toMap(message.getData());
        Integer status = Integer.valueOf(stringMap.get("status"));

        iflyLfasr.setStatus(status);
        iflyLfasrService.updateById(iflyLfasr);

        return LfasrStatuEnum.getByStatus(status).getName();
    }

    /**
     * 获取转写任务结果
     *
     * @param taskId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public String getResult(String taskId) {
        LambdaQueryWrapper<IflyLfasr> queryWrapper = Wrappers.<IflyLfasr>lambdaQuery().eq(IflyLfasr::getTaskId, taskId);
        IflyLfasr iflyLfasr = iflyLfasrService.getOne(queryWrapper);
        ValidateUtil.validateForResponse(Objects.isNull(iflyLfasr), "%s语音转写任务不存在", taskId);

        val hasResult = iflyLfasr.getStatus().equals(LfasrStatuEnum.REVERING_RESULT_UPLOAD.getCode()) && Strings.isNotBlank(iflyLfasr.getData());

        if (hasResult) {
            return iflyLfasr.getData();
        }
        try {
            // 更新转写任务状态
            this.getProgress(taskId);
        }catch (BusinessException e){
            log.info(e.getMessage());
        }
        Message result = lfasrClient.getResult(taskId);

        if (result.getOk() == 0) {
            String data = result.getData();
            iflyLfasr.setData(data);
            iflyLfasrService.updateById(iflyLfasr);
            return data;
        } else {
            throw new BusinessException(result.toString());
        }
    }
}
