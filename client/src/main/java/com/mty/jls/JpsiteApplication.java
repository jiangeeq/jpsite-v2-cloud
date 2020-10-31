package com.mty.jls;

import com.mty.jls.dovecommon.utils.SpringUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement  // 启用事务处理
public class JpsiteApplication {

    public static void main(String[] args) {
        final ConfigurableApplicationContext context = SpringApplication.run(JpsiteApplication.class, args);
        SpringUtil.setApplicationContext(context);
    }

}
