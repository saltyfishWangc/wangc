package com.wangc.base.aop.util;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collections;

/**
 * @author
 * @Description: 从aop通知的JoinPoint中获取参数
 * @date 2022/8/30 11:17
 */
@Slf4j
public class AopUtil {

    public static String getReqUri(JoinPoint joinPoint) {
        try {
            RequestMapping classMapping = joinPoint.getTarget().getClass().getAnnotation(RequestMapping.class);
            Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
            String classMappingPath = "";
            if (classMapping != null && classMapping.value() != null && classMapping.value().length > 0) {
                classMappingPath = classMapping.value()[0];
            }

            return classMappingPath + getMethodUri(method);
        } catch (Exception e) {
            log.error("getReqUri error ", e);
        }
        return "";
    }

    private static String getMethodUri(Method method) {
        String uri = "";
        if (method.isAnnotationPresent(RequestMapping.class)) {
            RequestMapping methodMapping = method.getAnnotation(RequestMapping.class);
            uri = (methodMapping.value() == null || methodMapping.value().length == 0) ? uri : methodMapping.value()[0];
        } else if (method.isAnnotationPresent(GetMapping.class)) {
            GetMapping methodMapping = method.getAnnotation(GetMapping.class);
            uri = (methodMapping.value() == null || methodMapping.value().length == 0) ? uri : methodMapping.value()[0];
        } else if (method.isAnnotationPresent(PostMapping.class)) {
            PostMapping methodMapping = method.getAnnotation(PostMapping.class);
            uri = (methodMapping.value() == null || methodMapping.value().length == 0) ? uri : methodMapping.value()[0];
        }
        return uri;
    }

}
