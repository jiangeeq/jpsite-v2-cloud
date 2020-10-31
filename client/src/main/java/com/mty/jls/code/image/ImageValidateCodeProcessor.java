package com.mty.jls.code.image;

import com.mty.jls.code.contract.AbstractValidateCodeProcessor;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.Objects;


/**
 * 图片验证码处理器
 *
 * @author 掘金-蒋老湿（公众号：十分钟学编程）
 *
 */
@Component
public class ImageValidateCodeProcessor extends AbstractValidateCodeProcessor<ImageCode> {

	/**
	 * 发送图形验证码，将其写到响应中
	 */
	@Override
	public void send(ServletWebRequest request, ImageCode imageCode) throws IOException {
		ImageIO.write(imageCode.getImage(), "JPEG", Objects.requireNonNull(request.getResponse()).getOutputStream());
	}

}
