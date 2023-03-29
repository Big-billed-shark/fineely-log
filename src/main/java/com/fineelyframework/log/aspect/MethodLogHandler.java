package com.fineelyframework.log.aspect;

import com.fineelyframework.log.exception.FineelyLogException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;

import java.time.LocalDateTime;

public interface MethodLogHandler {

    /**
     * 接口调用日志拦截记录方法（调用成功拦截）
     */
    Object interceptorHandler(ProceedingJoinPoint point) throws Throwable;

    /**
     * 接口调用日志拦截记录方法（调用失败拦截）
     */
    void printExceptionHandler(JoinPoint point, FineelyLogException ex);

    /**
     * 处理日志并存储
     */
    void handleOpenApiLog(JoinPoint point, Object result,
                                  LocalDateTime startTime, LocalDateTime endTime, double timeConsuming, String exceptionInfo);
}
