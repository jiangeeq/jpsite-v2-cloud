package com.mty.authserver;

import com.mty.jls.dovecommon.utils.SpringUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;


@SpringBootApplication
public class AuthServerApplication {

    public static void main(String[] args) {
        final ConfigurableApplicationContext context = SpringApplication.run(AuthServerApplication.class, args);
        SpringUtil.setApplicationContext(context);

    }

}
