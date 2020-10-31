package com.mty.jls.contract.enums;


/**
 *
 * 校验码类型
 *
 * @author 掘金-蒋老湿（公众号：十分钟学编程）
 *
 */
public enum ValidateCodeEnum {

	/**
	 * 短信验证码
	 */
	SMS {
		@Override
		public String getParamNameOnValidate() {
			return "smsCode";
		}
	},
	/**
	 * 图片验证码
	 */
	IMAGE {
		@Override
		public String getParamNameOnValidate() {
			return "imageCode";
		}
	};

	/**
	 * 校验时从请求中获取的参数的名字
	 * @return String
	 */
	public abstract String getParamNameOnValidate();

}
