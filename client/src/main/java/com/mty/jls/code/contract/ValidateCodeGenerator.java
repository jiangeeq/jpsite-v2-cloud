package com.mty.jls.code.contract;

import com.mty.jls.code.contract.ValidateCode;
import org.springframework.web.context.request.ServletWebRequest;

/**
 * 验证码生成器接口
 * @author 掘金-蒋老湿（公众号：十分钟学编程）
 *
 */
public interface ValidateCodeGenerator {

	/**
	 * 生成验证码
	 * @param request
	 * @return 验证码
	 */
	ValidateCode generate(ServletWebRequest request);

}
