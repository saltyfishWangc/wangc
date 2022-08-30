## 2022.08.29 SpringBoot整合Swagger2 
1. 引入对应依赖
```
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
```
2. 创建Swagger2Config.java配置类
启动项目访问http://localhost:8080/swagger-ui.html就可以看到swagger ui

3. 针对接口、实体类添加Swagger注解
```
@ApiModel 用于实体类上面说明
@ApiModelProperty 用于字段上说明
@Api 用于指定一个controller中各个接口的通用说明
@ApiOperation 用于说明一个请求接口
@ApiImplicitParam 用于说明一个请求参数
@ApiImplicitParams 用于包含多个@ApiImplicitParam，这种一般针对接口参数是Map的
请求参数也可以用@Parameter来标记，直接加到@RequestParam之前
@ApiIgnore 用于标记忽略不必要显示的参数
```
<br>
4. 接口文档导出
http://ip:port/v2/api-docs是个纯文件的html页面，里面是接口的json文本信息，复制保存成本地文本，即可导入到其他支持swagger接口的工具中去     
        
**Swagger有两个版本Swagger2、Swagger3**
Swagger3和Swagger2的区别：
1. 引入的依赖是：<br><br>
`<dependency>-->
     <groupId>io.springfox</groupId>
     <artifactId>springfox-boot-starter</artifactId>
     <version>3.0.0</version>
 </dependency>
 `

2. 配置类上注解是@EnableOpenApi
3. Swagger ui的访问地址是：http://ip:port/swagger-ui/index.html
4. 自带了一个是否开启swagger的配置：springfox.documentation.swagger-ui.enabled=true
5. 对于接口的标记不再是@ApiOperation，而是@Operation

## 2022.08.30 SpringBoot整合SpringBootStarterAop
1. 引入对应依赖
```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-aop</artifactId>
</dependency>
```
<br>
2. 创建切面RequestAspect.java

AOP注解说明：
```
@Aspect : 该注解要添加在类上，声明这是一个切面类，使用时需要与@Component注解一起用，表明同时将该类交给spring管理
@Pointcut : 用来定义一个切点，也就是要关注的某件事情的入口，切入点定义了事件触发时机。该注解要添加在方法上，该方法签名必须是public void类型，只需要方法签名，不需要在方法体内编写实际代码
该注解有两个常用的表达式：execution()和annotation()
@Pointcut("execution(* com.wangc.controller..*.*(..))")
@Pointcut("@annotation(org.springframework.web.bind.annotation.PostMapping)")
其中execution的表达式：
 1.第一个*:表示返回值类型，*表示所有类型；
 2.包名:标识需要拦截的包名；
 3.包名后的..:表示当前包和当前包的所有子包
 4.第二个*:表示类名，*表示所有类
 5.最后的*(..):星号表示方法名，*表示所有的方法，后面括弧里面表示方法的参数，两个句点表示任何参数
@Around : 该注解用于修饰Around增强处理
 1.当定义一个 Around 增强处理方法时，该方法的第一个形参必须是 ProceedingJoinPoint 类型（至少一个形参）。在增强处理方法体内，调用 ProceedingJoinPoint 的 proceed 方法才会执行目标方法：这就是 @Around 增强处理可以完全控制目标方法执行时机、如何执行的关键；如果程序没有调用 ProceedingJoinPoint 的 proceed 方法，则目标方法不会执行。
 2.调用 ProceedingJoinPoint 的 proceed 方法时，还可以传入一个 Object[] 对象，该数组中的值将被传入目标方法作为实参 —— 这就是 Around 增强处理方法可以改变目标方法参数值的关键。这就是如果传入的 Object[] 数组长度与目标方法所需要的参数个数不相等，或者 Object[] 数组元素与目标方法所需参数的类型不匹配，程序就会出现异常。
@Before : 该注解指定的方法在切面切入目标方法之前执行，可以做一些log处理，也可以做一些信息的统计。JoinPoint对象很有用，可以用它来获取一个签名，利用签名可以获取请求的包名、方法名，包括参数等
@After : 该注解指定方法在切面切入目标方法之后执行，和@Before注解相对应，也可以做一些完成某方法之后的log处理。
@AfterReturning : 和@After注解有些类似，区别在于@AfterReturning注解可以用来捕获切入方法执行完之后的返回值，对返回值进行业务逻辑上的增强护理。
注意：在 @AfterReturning 注解 中，属性 returning 的值必须要和参数保持一致，否则会检测不到。该方法中的第二个入参就是被切方法的返回值，在 doAfterReturning 方法中可以对返回值进行增强，可以根据业务需要做相应的封装。
@AfterThrowing : 当被切方法执行过程中抛出异常时，会进入 @AfterThrowing 注解的方法中执行，在该方法中可以做一些异常的处理逻辑。要注意的是 throwing 属性的值必须要和参数一致，否则会报错。该方法中的第二个入参即为抛出的异常。

```

注意：

1. 当一个方法同时被@Around、@Before、@After标记时，执行顺序是@Around -> @Before -> @Around.proceed() -> @After
2. @Before、@After、@Around、@AfterReturning、@AfterThrowing 在指定切点时也可以直接用定时切点时的execution表达式
@Before("execution(* com.wangc.controller..*.*(..))")，这样就少了@Pointcut("execution(* com.wangc.controller..*.*(..))")

这些注解都是aspectj包中的，所以这也是为什么spring aop中依赖了aspectj的原因
