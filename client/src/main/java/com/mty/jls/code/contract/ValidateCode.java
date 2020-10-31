package com.mty.jls.code.contract;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * 验证码对象基类
 *
 * @author 掘金-蒋老湿（公众号：十分钟学编程）
 *
 */
@Data
public class ValidateCode implements Serializable {
	private static final long serialVersionUID = 1588203828504660915L;

	private String code;

	private LocalDateTime expireTime;

	public ValidateCode(String code, int expireIn){
		this.code = code;
		this.expireTime = LocalDateTime.now().plusSeconds(expireIn);
	}

	public ValidateCode(String code, LocalDateTime expireTime){
		this.code = code;
		this.expireTime = expireTime;
	}

	public boolean isExpired() {
		return LocalDateTime.now().isAfter(this.expireTime);
	}
}
