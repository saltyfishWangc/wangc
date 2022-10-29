package com.wangc.config;

import com.wangc.log.interceptor.MyLogInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author
 * @Description:
 * @date 2022/10/27 20:38
 */
@Configuration
@ConditionalOnProperty(prefix = "mylog", name = "switch", havingValue = "true")
public class MyLogAutoConfiguration implements WebMvcConfigurer {

    //注册自定义日志拦截器
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new MyLogInterceptor());
    }
}
