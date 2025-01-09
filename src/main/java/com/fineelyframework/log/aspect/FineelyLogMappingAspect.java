package com.fineelyframework.log.aspect;

import com.fineelyframework.log.exception.FineelyLogException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;

@Aspect
@Component
public class FineelyLogMappingAspect {

    @Resource
    MethodLogHandler methodLogHandler;

    @Around(value = "@annotation(com.fineelyframework.log.annotation.FineelyLogMapping)")
    public Object aroundInterceptor(ProceedingJoinPoint point) throws Throwable {
        return methodLogHandler.interceptorHandler(point);
    }


    @AfterThrowing(pointcut = "@annotation(com.fineelyframework.log.annotation.FineelyLogMapping)", throwing = "ex")
    public void printException(JoinPoint point, FineelyLogException ex) {
        methodLogHandler.printExceptionHandler(point, ex);
    }
}
