<h2>2022.08.29 SpringBoot整合Swagger2</h2> 
1.引入对应依赖
<dependency>
            <groupId>io.springfox</groupId>
            <artifactId>springfox-swagger2</artifactId>
            <version>2.9.2</version>
        </dependency>
        <dependency>
            <groupId>io.springfox</groupId>
            <artifactId>springfox-swagger-ui</artifactId>
            <version>2.9.2</version>
        </dependency>
2.创建Swagger2Config配置类
启动项目访问http://localhost:8080/swagger-ui.html就可以看到swagger ui

3.针对接口、实体类添加Swagger注解
@ApiModel 用于实体类上面说明
@ApiModelProperty 用于字段上说明

@Api 用于指定一个controller中各个接口的通用说明
@ApiOperation 用于说明一个请求接口
@ApiImplicitParam 用于说明一个请求参数
@ApiImplicitParams 用于包含多个@ApiImplicitParam，这种一般针对接口参数是Map的
请求参数也可以用@Parameter来标记，直接加到@RequestParam之前
@ApiIgnore 用于标记忽略不必要显示的参数   

4.接口文档导出
http://ip:port/v2/api-docs是个纯文件的html页面，里面是接口的json文本信息，复制保存成本地文本，即可导入到其他支持swagger接口的工具中去     
        
Swagger有两个版本Swagger2、Swagger3
Swagger3和Swagger2的区别：
1.引入的依赖是：
<dependency>-->
    <groupId>io.springfox</groupId>
    <artifactId>springfox-boot-starter</artifactId>
    <version>3.0.0</version>
</dependency>
2.配置类上注解是@EnableOpenApi
3.Swagger ui的访问地址是：http://ip:port/swagger-ui/index.html
4.自带了一个是否开启swagger的配置：springfox.documentation.swagger-ui.enabled=true
5.对于接口的标记不再是@ApiOperation，而是@Operation