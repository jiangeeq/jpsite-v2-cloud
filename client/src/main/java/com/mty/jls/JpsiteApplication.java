package com.mty.jls;

import com.dove.jls.common.utils.SpringUtil;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableDubbo
@SpringBootApplication
@EnableTransactionManagement  // 启用事务处理
public class JpsiteApplication {

    public static void main(String[] args) {
        final ConfigurableApplicationContext context = SpringApplication.run(JpsiteApplication.class, args);
        SpringUtil.setApplicationContext(context);
    }

}
