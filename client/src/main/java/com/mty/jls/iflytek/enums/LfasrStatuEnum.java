package com.mty.jls.iflytek.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum LfasrStatuEnum {
    CREATE_SUCCESS(0,"任务创建成功"),
    UPLOAD_SUCCESS(1,"音频上传完成"),
    MERGE_SUCCESS(2,"音频合并完成"),
    REVERING(3,"音频转写中"),
    REVERING_RESULT_LOADING(4, "转写结果处理中"),
    REVER_SUCCESS(5, "转写完成"),
    REVERING_RESULT_UPLOAD(9, "转写结果上传完成"),
    DEFAULT(999, "未知结果");
    private Integer code;
    private String name;

    public static LfasrStatuEnum getByStatus(Integer status){
        for (LfasrStatuEnum statusEnum : LfasrStatuEnum.values()) {
            if(statusEnum.getCode().equals(status)){
                return statusEnum;
            }
        }
        return DEFAULT;
    }
}
