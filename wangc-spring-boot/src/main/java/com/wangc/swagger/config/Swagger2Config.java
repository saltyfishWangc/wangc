package com.wangc.swagger.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author
 * @Description: Swagger配置类
 * @date 2022/8/29 18:27
 */
@Configuration
@EnableSwagger2
/**
 * 通过配置文件中的swagger.enable来将生产环境屏蔽，在product对应profile下的配置文件中配置即可
 */
@ConditionalOnExpression("${swagger.enable:true}")
public class Swagger2Config {

    /**
     * 引入Swagger依赖，创建配置类后就可以访问http://localhost:8080/swagger-ui.html
     * @return
     */
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                // 扫描包
                .apis(RequestHandlerSelectors.basePackage("com.wangc"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                // 页面标题
                .title("Spring Boot 继承 Swagger2 接口文档")
                // 创建人
                .contact(new Contact("wangc", "http://www.baidu.com", "wangchao2@msok.com"))
                // 版本号
                .version("1.0")
                // 描述
                .description("API 描述，如有疑问，请联系wangc")
                .build();
    }
}
