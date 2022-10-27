package com.wangc.log.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author
 * @Description: 自定义添加日志的注解，用于标注哪些方法需要添加日志
 * @date 2022/10/27 20:22
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MyLog {

    /**
     * 方法描述
     * @return
     */
    String desc() default "";
}
