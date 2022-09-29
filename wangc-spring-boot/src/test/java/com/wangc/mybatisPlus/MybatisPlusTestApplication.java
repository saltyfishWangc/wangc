package com.wangc.mybatisPlus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author
 * @Description:
 * @date 2022/9/28 11:39
 */
@SpringBootApplication
@SpringBootConfiguration
@ComponentScan(basePackages = {"com.wangc.mybatisPlus.*"})
public class MybatisPlusTestApplication {

    public static void main(String... args) {
        SpringApplication.run(MybatisPlusTestApplication.class, args);
    }
}
