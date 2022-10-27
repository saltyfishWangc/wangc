package com.wangc.log.interceptor;

import com.wangc.log.annotation.MyLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * @author
 * @Description: 自定义日志拦截器
 * @date 2022/10/27 20:25
 */
@Slf4j
public class MyLogInterceptor extends HandlerInterceptorAdapter {

    private static final ThreadLocal<Long> startTimeThreadLocal = new ThreadLocal<>();

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod(); //获得被拦截的方法对象
        MyLog myLog = method.getAnnotation(MyLog.class); //获得方法上的注解
        if (Objects.nonNull(myLog)) {
            //被MyLog注解标注的方法需要进行日志记录
            startTimeThreadLocal.set(System.currentTimeMillis());
        }
        //放行
        return true;
    }

    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod(); //获得被拦截的方法对象
        MyLog myLog = method.getAnnotation(MyLog.class); //获得方法上的注解
        if (Objects.nonNull(myLog)) {
            //被MyLog注解标注的方法需要进行日志记录
            long endTime = System.currentTimeMillis();
            long startTime = startTimeThreadLocal.get();
            long optTime = endTime - startTime;

            String requestUri = request.getRequestURI();
            String methodName = method.getDeclaringClass().getName() + "." + method.getName();
            String methodDesc = myLog.desc();

            log.info("请求uri：{}，请求方法名：{}，方法描述：{}，方法执行时间：{}ms", requestUri, methodName, methodDesc, optTime);
        }
    }
}
