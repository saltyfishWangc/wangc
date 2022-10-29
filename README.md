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
针对上面的，只要我们引入了这个starter的依赖，那就生效了。那么像MyLog这个功能，想给他加个开关来控制不生效怎么处理？
假设项目中是个很老的项目了，早就引入了这个starter并且一直在用，那我现在想要关掉这个功能，总不能直接把这个starter的
依赖去掉吧，你去掉依赖，那么对应代码中的@MyLog也得去掉，不然编译都有问题，那么这个改动岂不是很大了。
鉴于上面这个问题？是否可以在这个strater中设置一个总开关，这个开关是开是关由用户自己决定。那怎么做呢？
在com.wangc.config.MyLogAutoConfiguration上加一个注解@ConditionalOnProperty(prefix = "mylog", name = "switch", havingValue = "true")
表示只有当配置文件中存在myLog.switch的配置且值是true的时候，MyLogAutoConfiguration的@Configuration才会生效，
否则就不生效。这样就不用去掉哪些@MyLog代码也不会报错，同时这个功能也被关掉了。

注意：上面的这个MyLog可以这么用，因为com.wangc.config.MyLogAutoConfiguration是没有在哪个代码中被注入的，所以这里
@Configuration没生效，那是不会报错的。如果存在有代码要注入这个bean的，那这里没生效，启动的时候就会报错，因为容器里找不到
bean

##### 案例二：自定义ImportSelector类 + @Import
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

2. 自定义两个类：AService、BService
```
@Slf4j
public class AService {

    public void say() {
        log.info("我是A");
    }
}

@Slf4j
public class BService {

    public void say() {
        log.info("我是B");
    }
}
```
注意：这两个类就是普通的Java类，没有被@Component标记

3. 自定义ServiceImportSelector，实现Spring的ImportSelector接口，实现其selectImports方，将要自动装在的类名在这方法返回
```
/**
 * @author
 * @Description: 自定义ImportSelector，指定引入类，结合@Import使用来实现自动装载
 * 实现selectImports方法，这个方法返回的类名会被自动装配到IOC中
 * @date 2022/10/29 10:37
 */
public class ServiceImportSelector implements ImportSelector {

    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        return new String[]{AService.class.getName(), BService.class.getName()};
    }
}
```

4. 自定义@EnableServiceAutoImport注解，这个注解要用到@Import，来指定引入上面的ServiceImportSelector类
```
/**
 * 自定义注解用于自动装配的开关，引入ServiceImportSelector
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Import(ServiceImportSelector.class)
public @interface EnableServiceAutoImport {
}
```

到这里，自定义的starter就完成了，下面是使用这个starter
5. 引入该starter，然后在启动类上加上@EnableServiceAutoImport
```
@SpringBootApplication
@EnableServiceAutoImport
public class StarterTestServer {

    public static void main(String... args) {
        SpringApplication.run(StarterTestServer.class, args);
    }
}
```

6. 直接注入AService、BService
```
@RestController
public class TestController {

    @Autowired
    private HelloService helloService;

    @Autowired
    private AService aService;

    @RequestMapping("/test/testStarter")
    @MyLog(desc = "testStarter")
    public String testStarter() {
        aService.say();
        return helloService.sayHello();
    }
}
```

注意：第二种自定义ImportSelector类 + @Import的方式就没有去META-INFspring.factories中添加需要自动装配的类，替换它的是由
ImportSelector的selectImports方法

#### 原理
自定义spring-boot-starter有两种方式，一种是主动方法、一种是被动方法。
1. 主动方式
在META-INF/spring.factories中定义好自动装配的类，这样引入后就会加载自动装配的类，这是springboot的spi机制，是模仿java的spi

2. 被动方式
通过自定义@Enable**，里面通过@Import引入自定义的ImportSelector，实现selectImports方法，将指定的类加载到IOC中。然后使用端通过
在启动类上加@Enable**来生效自动装配。

原理：
<https://www.processon.com/outline/635cd4455653bb070e1dcab8>

