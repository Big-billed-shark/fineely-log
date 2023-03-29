package com.fineelyframework.log.aspect;

import com.alibaba.fastjson2.JSONObject;
import com.fineelyframework.log.annotation.FineelyLog;
import com.fineelyframework.log.annotation.FineelyLogMapping;
import com.fineelyframework.log.constants.CommonConstants;
import com.fineelyframework.log.dao.MethodLogDao;
import com.fineelyframework.log.entity.MethodLogEntity;
import com.fineelyframework.log.exception.FineelyLogException;
import com.fineelyframework.log.strategy.ReplaceStrategy;
import com.fineelyframework.log.strategy.SimpleStrategy;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class DefaultMethodLogHandler implements MethodLogHandler {

    @Resource
    private MethodLogDao methodLogDao;

    /**
     * 接口调用日志拦截记录方法（调用成功拦截）
     *
     * @param point
     */
    @Override
    public Object interceptorHandler(ProceedingJoinPoint point) throws Throwable {
        // 记录开始执行时间
        LocalDateTime startTime = LocalDateTime.now();
        long start = System.currentTimeMillis();
        // 执行业务方法并返回结果
        Object result = point.proceed();
        long end = System.currentTimeMillis();
        // 记录结束时间
        LocalDateTime endTime = LocalDateTime.now();
        // 处理日志并存储
        handleOpenApiLog(point, result, startTime, endTime, end - start, null);
        // 返回业务方法的返回值
        return result;
    }

    /**
     * 接口调用日志拦截记录方法（调用失败拦截）
     *
     * @param point
     * @param ex
     */
    @Override
    public void printExceptionHandler(JoinPoint point, FineelyLogException ex) {
        // 记录开始执行时间
        LocalDateTime startTime = LocalDateTime.now();
        // 获取异常信息
        String message = ex.getMessage();
        StackTraceElement[] stackTrace = ex.getStackTrace();
        String stackTraceMsg = Arrays.stream(stackTrace).map(StackTraceElement::toString).collect(Collectors.joining("  "));
        String errorResult = String.format("message: %s; stackTraceMsg: %s", message, stackTraceMsg);
        // 处理日志并存储
        handleOpenApiLog(point, null, startTime, null, 0, errorResult);
    }

    /**
     * 处理日志并存储
     */
    @Override
    public void handleOpenApiLog(JoinPoint point, Object result,
                                 LocalDateTime startTime, LocalDateTime endTime, double timeConsuming, String exceptionInfo) {
        MethodSignature methodSignature = (MethodSignature) point.getSignature();
        String methodName = methodSignature.getName();
        // 从参数中获取request对象
        HttpServletRequest request = MethodLogUtils.getRequest(methodName);
        // 获取IP地址
        String ipAddress = MethodLogUtils.getIpAddress(methodName, request);
        // 获取方法输入参数
        String args = MethodLogUtils.getArgsContent(methodName, point.getArgs());
        // 获取方法返回参数
        String returning = MethodLogUtils.getReturning(methodName, result);
        // 获取当前执行方法的信息
        Method method = methodSignature.getMethod();
        String desc;
        RequestMethod[] methods;
        String module;
        String url;
        FineelyLog fineelyLog = method.getAnnotation(FineelyLog.class);
        if (Objects.nonNull(fineelyLog)) {
            desc = fineelyLog.desc();
            methods = fineelyLog.method();
            module = fineelyLog.module();
            url = fineelyLog.url();
        } else {
            FineelyLogMapping annotation = method.getAnnotation(FineelyLogMapping.class);
            desc = annotation.desc();
            methods = annotation.method();
            module = annotation.module();
            url = annotation.url();
        }
        // 获取简单替换策略替换desc
        ReplaceStrategy replaceStrategy = new SimpleStrategy();
        // 添加返回结果对象 到替换策略中
        replaceStrategy.appendParamMap("result", JSONObject.toJSONString(result));
        replaceStrategy.appendParamMap("methodName", method.getName());
        replaceStrategy.appendParamMap("startTime", startTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        if (Objects.nonNull(endTime)) {
            replaceStrategy.appendParamMap("endTime", endTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        }
        // 请求参数
        Map<String, String[]> requestParameterMap = request.getParameterMap();
        if (Objects.nonNull(requestParameterMap)) {
            Map<String, String> map = new HashMap<>(requestParameterMap.size());
            requestParameterMap.forEach((key, value) -> map.put(key, value[0]));
            replaceStrategy.appendParamMap("request", JSONObject.toJSONString(map));
        }

        String[] argNames = methodSignature.getParameterNames();
        for (int i = 0; i < argNames.length; i++) {
            Object arg = point.getArgs()[i];
            Object json = JSONObject.toJSONString(arg);
            replaceStrategy.appendParamMap(argNames[i], json);
        }

        String finishedDesc = replaceStrategy.execute(desc).complete();

        // 创建日志对象
        MethodLogEntity methodLog = new MethodLogEntity();
        methodLog.setDesc(finishedDesc);

        // 设置时间参数
        methodLog.setStartTime(startTime);
        methodLog.setEndTime(endTime);
        // 调用异常时，没有结束时间
        methodLog.setTimeConsuming(timeConsuming);
        methodLog.setMethodName(methodName);
        methodLog.setAllParams(args);
        methodLog.setResult(returning);
        methodLog.setIpAddress(ipAddress);
        methodLog.setExceptionInfo(exceptionInfo);
        methodLog.setCreateTime(startTime);
        methodLog.setMethod(methods);
        methodLog.setModule(module);
        if (StringUtils.isNotEmpty(url)) {
            methodLog.setUrl(url);
        } else {
            methodLog.setUrl(request.getRequestURI());
        }
        String logOperator = request.getParameter(CommonConstants.FINEELY_LOG_OPERATOR);
        if (StringUtils.isNoneEmpty(logOperator)) {
            methodLog.setOperator(logOperator);
        } else {
            methodLog.setOperator("system");
        }
//         存储接口调用日志
        methodLogDao.saveLog(methodLog);
    }
}
