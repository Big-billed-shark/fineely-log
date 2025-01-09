package com.fineelyframework.log.aspect;

import com.fineelyframework.log.exception.FineelyLogException;
import jakarta.annotation.Resource;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;



@Aspect
@Component
public class FineelyLogAspect {

    @Resource
    MethodLogHandler methodLogHandler;

    @Around(value = "@annotation(com.fineelyframework.log.annotation.FineelyLog)")
    public Object aroundInterceptor(ProceedingJoinPoint point) throws Throwable {
        return methodLogHandler.interceptorHandler(point);
    }


    @AfterThrowing(pointcut = "@annotation(com.fineelyframework.log.annotation.FineelyLog)", throwing = "ex")
    public void printException(JoinPoint point, FineelyLogException ex) {
        methodLogHandler.printExceptionHandler(point, ex);
    }
}
