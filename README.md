# wangc
基于SpringBoot创建工程，后面逐步整合其他框架
## 整合Swagger
见wangc-spring-boot模块README.md

## 引入spring-boot-starter-aop
见wangc-spring-boot模块README.md

## 整合ElasticSearch
见wangc-spring-boot模块README.md

## 整合Mybatis-plus
见wangc-spring-boot模块README.md

## 整合shardingsphere的sharding-jdbc
见wangc-spring-boot模块README.md

## 自定义Spring Boot Satrter
### 自动装配原理
```
//Java元注解，作用是修饰自定义注解的注解
@Target(ElementType.TYPE) //定义注解的作用范围。如类、方法、属性
@Retention(RetentionPolicy.RUNTIME) //定义注解的生命周期(注解保留多久)。如编译器(像@Override就是在编译器，代码实际运行时，这个注解是不起作用的)、运行期
@Documented //javadoc 这个是要结合@param、@see这些注解的，会显示文档
@Inherited //修饰的自定义注解可被子类继承
```

@Configuration和@Component的区别：
```
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface Configuration {
    @AliasFor(annotation = Component.class)
    String value() default "";
    
    boolean proxyBeanMethods() default true;
}


@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Indexed
public @interface Component {
    String value() default "";
}       
```
通过对比上面两者的源码可以知道：
1. @Configuration本质上还是@Component
2. Spring容器在启动时，会加载默认的一些PostProcessor，其中就有ConfugurationPostProcessor，这个后置处理器专门处理被
@Configuration注解的类，这个程序会在bean定义加载完成之后，在bean初始化前进行处理。
3. @Configuration多了个proxtBeanMethods，默认为true。这个表示生成CGLIB代理类，而@Component不会生成代理类。如果proxtBeanMethods == false，
那么，@Configuration和@Component就没什么区别了。下面案例说明proxtBeanMethods = true时，@Configuration和@Component的区别：
```
@Configuration
public class ComponentConfig {
    @Bean
    public Country country() {
        return new Country();
    }
    @Bean
    public UserInfo userInfo() {
        return new UserInfo(country());
    }
}

@Component
public class ComponentConfig {
    @Bean
    public Country country() {
        return new Country();
    }
    @Bean
    public UserInfo userInfo() {
        return new UserInfo(country());
    }
}

@SpringBootTest
public class ComponentAndConfigurationTest {
    @Autowired
    private Country country;
    
    @Autowired
    private UserInfo userInfo;
    
    @Test
    public void componentTest() {
        boolean result = userInfo.getCountry() == country;
        System.out.println(result ? "同一个country" : "不同的country");
    } 
}
```
测试结果：当采用@Configuration时，是同一个country。@Component时，不是同一个country。
采用@Configuration时，判断到@Bean，会去spring容器里查是否有对应的cglib代理类，有的话就返回，而不是直接调用country()生成一个新的。所以是单例的。
而@Component则不会这么做，调用country()时，就直接执行这个方法的逻辑了，返回的一个新的Country实例，并且还不归Spring容器管。

#### 自定义spring boot starter
##### 案例一：@Configuration + @Bean + META-INFO/spring.factories配置org.springframework.boot.autoconfigure.EnableAutoConfiguration=
1. 引入依赖
```
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>2.2.2.RELEASE</version>
</parent>
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-autoconfigure</artifactId>
    </dependency>
</dependencies>
```
2. 创建配置属性类HelloProperties

3. 创建服务类HelloService

4. 创建自动配置类HelloServiceAutoConfiguration

5. 在resource目录下创建META-INF/spring.factories添加如下映射：
org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
  com.wangc.config.HelloServiceAutoConfiguration
  
6. 使用starter
6.1 引入自定义starter依赖
```
<dependency>
    <groupId>com.wangc</groupId>
    <artifactId>hello-spring-boot-starter</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```  
6.2 application.yml文件添加配置
```
hello:
    name: wangc
    address: hubei
```

6.3 在代码中注入HelloService就可以了
```
@Autowired
private HelloService helloService;
```

7. 扩展自定义日志拦截器
7.1 追加依赖
```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-configuration-processor</artifactId>
</dependency>
```

7.2 自定义MyLog注解

7.3 自定义日志拦截器MyLogInterceptor

7.4 创建自动配置类MyLogAutoConfiguration，用于自动配置拦截器，参数解析器等web组件

7.5 在META-spring.factories中追加MyLogAutoConfigutation配置类
```
org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
  com.wangc.config.HelloServiceAutoConfiguration,\
  com.wangc.config.MyLogAutoConfiguration
```

7.6 在web方法上加上@MyLog注解
```
@RestController
@RequestMapping("/hello")
public class HelloController {
    //HelloService在我们自定义的starter中已经完成了自动配置，所以此处可以直接注入
    @Autowired
    private HelloService helloService;
    
    @MyLog(desc = "sayHello方法") //日志记录注解
    @GetMapping("/say")
    public String sayHello() {
        return helloService.sayHello();
    }
}
```
##### 案例二：

