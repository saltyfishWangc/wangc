package com.wangc.config;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author
 * @Description: 指定读取配置文件中前缀为hello的配置转换为Bean
 * @ConfigurationProperties(prefix = "hello")：如果单使用@ConfigurationProperties注解是没有任意意义的，它不能完成配置文件中hello开头的配置和HelloProperties的数据绑定
 * 两种用法：
 * @Component + @ConfigurationProperties 一起使用：@Component注解将该类加入到IOC容器中，过程中会根据@ConfigurationProperties指定的配置和HelloProperties数据绑定
 *
 * @EnableConfigurationProperties(HelloProperties.class) + @ConfigurationProperties 组合使用：
 * @EnableConfugurationProperties的作用是：让指定使用了@ConfigurationProperties注解的类生效，并且将该类注入到IOC容器中，这个过程中会根据@ConfigurationProperties指定的配置和HelloProperties数据绑定
 *
 * 注意看：第一个方式@Component + @ConfigurationProperties太呆板了，只要使用了，那就是被容器管理了，不适用于spring boot starter
 * 第二种方式适合，因为可以去META-INFO/spring.factories下加上配置类，那么才会加载，如果不配，那就不加载
 * @date 2022/10/27 19:48
 */
@Data
@Getter
@Setter
@ToString
@ConfigurationProperties(prefix = "hello")
public class HelloProperties {

    private String name;

    private String address;

}
