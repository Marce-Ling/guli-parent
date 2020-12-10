package com.atguigu.oss;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @Author Administrator
 * @CreateTime 2020-11-21
 * @Description
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableScheduling
public class OssApplication {

    public static void main(String[] args) {
        SpringApplication.run(OssApplication.class, args);
    }

}
