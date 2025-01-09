package com.fineelyframework.log.utils;

import com.alibaba.fastjson2.JSONObject;
import com.fineelyframework.log.entity.ParamMap;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;

public class MethodLogUtils {

    public static HttpServletRequest getRequest() {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            return attributes.getRequest();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getIpAddress(HttpServletRequest request) {
        try {
            return IpUtil.getIpAddress(request);
        } catch (Exception e) {
            return "Get failure";
        }
    }

    public static String getReturning(Object returning) {
        try {
            return JSONObject.toJSONString(returning);
        } catch (Exception e) {
            return "Get failure";
        }
    }

    public static String getArgsContent(Object[] args) {
        if (Objects.isNull(args) || args.length == 0) {
            return null;
        }
        try {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < args.length; i++) {
                Object arg = args[i];
                if (arg == null || arg instanceof ParamMap || arg instanceof HttpServletRequest || arg instanceof HttpServletResponse) {
                    continue;
                }
                builder.append(arg instanceof String ? arg : JSONObject.from(arg).toJSONString());
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
