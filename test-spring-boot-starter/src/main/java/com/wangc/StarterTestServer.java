package com.wangc;

import com.wangc.log.annotation.EnableServiceAutoImport;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author
 * @Description:
 * @date 2022/10/28 15:32
 */
@SpringBootApplication
@EnableServiceAutoImport
public class StarterTestServer {

    public static void main(String... args) {
        SpringApplication.run(StarterTestServer.class, args);
    }
}
