package com.fineelyframework.log.aspect;

import com.alibaba.fastjson.JSONObject;
import com.fineelyframework.log.utils.IpUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

public class MethodLogUtils {

    public static HttpServletRequest getRequest(String methodName) {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            return attributes.getRequest();
        } catch (Exception e) {
            throw new IllegalStateException("当前执行的方法：" + methodName + " 获取HttpServletRequest参数失败，请联系开发人员！");
        }
    }

    public static String getIpAddress(String methodName, HttpServletRequest request) {
        try {
            return IpUtil.getIpAddress(request);
        } catch (Exception e) {
            return "Get failure";
        }
    }

    public static String getReturning(String methodName, Object returning) {
        if (Objects.isNull(returning)) {
            throw new IllegalStateException("当前执行的方法：" + methodName + " 返回值为空，请联系开发人员修改！");
        }
        try {
            return JSONObject.toJSONString(returning);
        } catch (Exception e) {
            return "Get failure";
        }
    }

    public static String getArgsContent(String methodName, Object[] args) {
        if (Objects.isNull(args) || args.length == 0) {
            return null;
        }
        try {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < args.length; i++) {
                Object arg = args[i];
                if (arg == null || arg instanceof HttpServletRequest || arg instanceof HttpServletResponse) {
                    continue;
                }
                builder.append(arg);
                if (i < args.length - 1) {
                    builder.append(",");
                }
            }
            String parameter = builder.toString();
            if (StringUtils.isBlank(parameter)) {
                return null;
            }
            return parameter;
        } catch (Exception e) {
            return null;
        }
    }

}
