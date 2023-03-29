package com.fineelyframework.log.aspect;

import com.fineelyframework.log.exception.FineelyLogException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Aspect
@Component
public class FineelyLogAspect {

    @Resource
    MethodLogHandler methodLogHandler;

    /**
     * 接口调用日志拦截记录方法（调用成功拦截）
     *
     * @param point
     */
    @Around(value = "@annotation(com.fineelyframework.log.annotation.FineelyLog)")
    public Object aroundInterceptor(ProceedingJoinPoint point) throws Throwable {
        return methodLogHandler.interceptorHandler(point);
    }


    /**
     * 接口调用日志拦截记录方法（调用失败拦截）
     *
     * @param point
     * @param ex
     */
    @AfterThrowing(pointcut = "@annotation(com.fineelyframework.log.annotation.FineelyLog)", throwing = "ex")
    public void printException(JoinPoint point, FineelyLogException ex) {
        methodLogHandler.printExceptionHandler(point, ex);
    }
}
