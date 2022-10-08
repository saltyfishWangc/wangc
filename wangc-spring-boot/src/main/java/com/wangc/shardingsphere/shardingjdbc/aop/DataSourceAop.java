package com.wangc.shardingsphere.shardingjdbc.aop;

import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.api.hint.HintManager;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @author
 * @Description:
 * 对于生产上读写分离(这个读写分离是代码控制的，并不是mycat这种来控制)的应用，有的查从库查不到数据，这是因为会存在主从同步的问题，
 * 这种情况下需要开发介入，通过代码来切换数据源，让一些查询语句访问主数据源
 * @date 2022/10/8 20:01
 */
@Component
@Aspect
@Slf4j
public class DataSourceAop {

    /**
     * 定义切点
     * 范文：被@MasterDataSource标注的方法
     */
    @Pointcut("@annotation(com.wangc.shardingsphere.shardingjdbc.aop.annotation.MasterDataSource)")
    public void pointCut() {}

    /**
     * 定义切点
     * 范文：被@MasterDataSourceRevoke标注的方法
     */
    @Pointcut("@annotation(com.wangc.shardingsphere.shardingjdbc.aop.annotation.MasterDataSourceRevoke)")
    public void changeMasterDataSourceRevoke() {}

    /**
     * 直接在被注解标识的方法上切换数据源。调用后换回默认的数据源
     * @param joinPoint
     */
    @Around("pointCut()")
    public Object doAround(ProceedingJoinPoint joinPoint) {
        Object obj = null;
        try (HintManager hintManager = HintManager.getInstance()) {
            hintManager.setMasterRouteOnly();
            log.info("切换成主数据源");
            obj = joinPoint.proceed();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return obj;
    }

    /**
     * 被注解标识的方法被调用后返回null，切换数据源再调一次
     * @param joinPoint
     */
    @Around("changeMasterDataSourceRevoke()")
    public Object dochangeMasterDataSourceRevokeAround(ProceedingJoinPoint joinPoint) {
        Object obj = null;
        try {
            obj = joinPoint.proceed();
            log.info("返回：" + String.valueOf(obj));
            if (Objects.isNull(obj)) {
                try (HintManager hintManager = HintManager.getInstance()) {
                    hintManager.setMasterRouteOnly();
                    log.info("切换成主数据源");
                    obj = joinPoint.proceed();
                    log.info("返回：" + String.valueOf(obj));
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return obj;
    }

    /**
     * 被标注的方法访问null后切换数据源再调一次
     *
     * 适用场景：一种兜底方案，从库没查到再到主库查一次，第一次就从从库中查到了，就不用到主库中去查
     * @param joinPoint
     * @param result
     */
    @AfterReturning(value = "pointCut()", returning = "result")
    public void doAfterReturning(JoinPoint joinPoint, Object result) {}
}
