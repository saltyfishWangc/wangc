package com.wangc.shardingsphere.shardingjdbc;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author
 * @Description:
 * @date 2022/10/8 16:18
 */
@SpringBootApplication
@SpringBootConfiguration
@ComponentScan(basePackages = {"com.wangc.shardingsphere.shardingjdbc.*"})
public class ShardingJdbcTestApplication {
}
