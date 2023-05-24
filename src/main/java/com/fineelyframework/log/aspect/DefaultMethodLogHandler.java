package com.fineelyframework.log.aspect;

import com.alibaba.fastjson2.JSONObject;
import com.fineelyframework.log.annotation.FineelyLog;
import com.fineelyframework.log.annotation.FineelyLogMapping;
import com.fineelyframework.log.constants.CommonConstants;
import com.fineelyframework.log.dao.MethodLogDao;
import com.fineelyframework.log.entity.MethodLogEntity;
import com.fineelyframework.log.entity.ParamMap;
import com.fineelyframework.log.exception.FineelyLogException;
import com.fineelyframework.log.strategy.ReplaceStrategy;
import com.fineelyframework.log.strategy.SimpleStrategy;
import com.fineelyframework.log.utils.MethodLogUtils;
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

    @Override
    public Object interceptorHandler(ProceedingJoinPoint point) throws Throwable {
        LocalDateTime startTime = LocalDateTime.now();
        long start = System.currentTimeMillis();
        Object result = point.proceed();
        long end = System.currentTimeMillis();
        LocalDateTime endTime = LocalDateTime.now();
        handleOpenApiLog(point, result, startTime, endTime, end - start, null);
        return result;
    }

    @Override
    public void printExceptionHandler(JoinPoint point, FineelyLogException ex) {
        LocalDateTime startTime = LocalDateTime.now();
        String message = ex.getMessage();
        StackTraceElement[] stackTrace = ex.getStackTrace();
        String stackTraceMsg = Arrays.stream(stackTrace).map(StackTraceElement::toString).collect(Collectors.joining("  "));
        String errorResult = String.format("message: %s; stackTraceMsg: %s", message, stackTraceMsg);
        handleOpenApiLog(point, null, startTime, null, 0, errorResult);
    }

    @Override
    public void handleOpenApiLog(JoinPoint point, Object result,
                                 LocalDateTime startTime, LocalDateTime endTime, double timeConsuming, String exceptionInfo) {
        MethodSignature methodSignature = (MethodSignature) point.getSignature();
        String methodName = methodSignature.getName();
        HttpServletRequest request = MethodLogUtils.getRequest();
        String ipAddress = MethodLogUtils.getIpAddress(request);
        String args = MethodLogUtils.getArgsContent(point.getArgs());
        String returning = MethodLogUtils.getReturning(result);
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
        Object[] originalArgs = point.getArgs();
        ReplaceStrategy replaceStrategy = new SimpleStrategy();
        for (Object originalArg : originalArgs) {
            if (originalArg instanceof ParamMap) {
                replaceStrategy.appendParamMap((ParamMap) originalArg);
            }
        }
        replaceStrategy.appendParamMap("result", JSONObject.toJSONString(result));
        replaceStrategy.appendParamMap("methodName", method.getName());
        replaceStrategy.appendParamMap("startTime", startTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        if (Objects.nonNull(endTime)) {
            replaceStrategy.appendParamMap("endTime", endTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        }
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

        MethodLogEntity methodLog = new MethodLogEntity();
        methodLog.setDesc(finishedDesc);
        methodLog.setStartTime(startTime);
        methodLog.setEndTime(endTime);
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
        methodLogDao.saveLog(methodLog);
    }
}
