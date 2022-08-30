package com.wangc.base.aop;

import com.wangc.base.aop.util.AopUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

/**
 * @author
 * @Description:
 *  web请求aop切面
 *  1.当一个方法同时被@Around、@Before、@After标记时，执行顺序是@Around -> @Before -> @Around.proceed() -> @After
 *  2.@Before、@After、@Around、@AfterReturning、@AfterThrowing 在指定切点时也可以直接用定时切点时的execution表达式
 *  @Before("execution(* com.wangc.controller..*.*(..))")，这样就少了@Pointcut("execution(* com.wangc.controller..*.*(..))")
 *  定义切点这一步
 *
 * @date 2022/8/30 9:53
 */
@Component
@Aspect
@Slf4j
public class RequestAspect {

    /**
     * 指明切点，也就是从哪些方法进行切入
     */
    @Pointcut("execution(* com.wangc.controller..*.*(..))")
    public void pointCut() {
        // 这个方法只是为了定义切点，后面告诉通知，本身没有任何业务逻辑
    }

    /**
     * 通过@annotation指明切点，这样所有被对应注解标记的方法都会成为切点，也就是从哪些方法进行切入
     */
    @Pointcut("@annotation(org.springframework.web.bind.annotation.PostMapping)")
    public void pointCutForAnnotation() {
        // 这个方法只是为了定义切点，后面告诉通知，本身没有任何业务逻辑
    }

    /**
     * 针对切点定义环绕通知，在调用方法前、后都做处理
     * @param joinPoint
     * @return
     */
    @Around("pointCut()")
    public Object doAround(ProceedingJoinPoint joinPoint) {
        log.info("-----------doAround----------");
        Object obj = null;
        long startTime = System.currentTimeMillis();
        try {
            obj = joinPoint.proceed();
            log.info("-----------doArounding----------");
            log.info("方法：{} 处理完成耗时：{}s", AopUtil.getReqUri(joinPoint), (System.currentTimeMillis() - startTime)/1000);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return obj;
    }

    /**
     * 针对切点定义前置通知，在调用方法前做处理，没有返回结果
     * @param joinPoint
     */
    @Before("pointCut()")
    public void doBefore(JoinPoint joinPoint) {
        log.info("-----------doBefore----------");
    }

    /**
     * 针对切点定义后置通知，在调用方法返回后做处理
     * @param joinPoint
     */
    @After("pointCut()")
    public void doAfter(JoinPoint joinPoint) {
        log.info("-----------doAfter----------");
    }

    /**
     * 后置返回通知
     *  如果第一个参数为JoinPoint，则第二个参数为返回值的信息
     *  如果第一个参数不为JoinPoint，则第一个参数为returning中对应的参数
     *  returning:限定了只有目标方法返回值与通知方法参数类型匹配时才能执行后置返回通知，否则不执行，参数为Object类型将匹配任何目标返回值
     *
     * @param joinPoint
     * @param result
     */
//    @AfterReturning(pointcut = "pointCut()", returning = "result")
//    public void doAfterReturning(JoinPoint joinPoint, Object result) {
//        log.info("-----------doAfterReturning result = {}", result);
//    }

    /**
     * 后置返回通知
     *  经测试，这个通知是一定会收到方法调用的返回，就算目标方法执行过程抛异常了，最终这里的方法还是会执行，只是获得的result为null
     *  可以理解，只有一个参数的AfterReturning是在代理方法的finally中调用的
     *
     * @param result
     */
    @AfterReturning(pointcut = "pointCut()", returning = "result")
    public void doAfterReturning(Object result) {
        log.info("-----------doAfterReturning result = {}", result);
    }

//    @AfterThrowing(pointcut = "pointCut()", throwing = "t")
//    public void doAfterThrowing(JoinPoint joinPoint, Throwable t) {
//        log.info("-----------doAfterThrowing throwable = {}", t.toString());
//    }

    @AfterThrowing(pointcut = "pointCut()", throwing = "t")
    public void doAfterThrowing(Throwable t) {
        log.info("-----------doAfterThrowing throwable = {}", t.toString());
    }

}
