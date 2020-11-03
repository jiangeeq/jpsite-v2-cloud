package com.mty.authserver;

import com.dove.jls.common.utils.SpringUtil;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;


@EnableDubbo
@SpringBootApplication
public class AuthServerApplication {

    public static void main(String[] args) {
        final ConfigurableApplicationContext context = SpringApplication.run(AuthServerApplication.class, args);
        SpringUtil.setApplicationContext(context);

    }

}
