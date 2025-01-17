package com.fineelyframework.log.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

public class MethodLogEntity implements Serializable {

    private Object[] method;
    private String methodName;
    private String module;
    private String url;
    private String desc;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private double timeConsuming;
    private String allParams;
    private String result;
    private String ipAddress;
    private String exceptionInfo;
    private String operator;
    private LocalDateTime createTime;

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getAllParams() {
        return allParams;
    }

    public void setAllParams(String allParams) {
        this.allParams = allParams;
    }

    public Object[] getMethod() {
        return method;
    }

    public void setMethod(Object[] method) {
        this.method = method;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public double getTimeConsuming() {
        return timeConsuming;
    }

    public void setTimeConsuming(double timeConsuming) {
        this.timeConsuming = timeConsuming;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getExceptionInfo() {
        return exceptionInfo;
    }

    public void setExceptionInfo(String exceptionInfo) {
        this.exceptionInfo = exceptionInfo;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        StringBuilder value = new StringBuilder();
        value.append("{\"allParams\":\"").append(allParams)
                .append("\",\"createTime\":\"").append(localDateTimeToString(createTime))
                .append("\",\"desc\":\"").append(desc)
                .append("\",\"endTime\":\"").append(localDateTimeToString(endTime))
                .append("\",\"ipAddress\":\"").append(ipAddress)
                .append("\",\"method\":").append(arraysToString(method))
                .append(",\"methodName\":\"").append(methodName)
                .append("\",\"module\":\"").append(module)
                .append("\",\"operator\":\"").append(operator)
                .append("\",\"result\":\"").append(result)
                .append("\",\"startTime\":\"").append(localDateTimeToString(startTime))
                .append("\",\"timeConsuming\":").append(timeConsuming)
                .append(",\"url\":\"").append(url).append("\"}");
        return value.toString();
    }

    private String localDateTimeToString(LocalDateTime localDateTime) {
        return localDateTime.toString().replace("T", " ");
    }

    private String arraysToString(Object[] a) {
        // ["GET"]
        if (a == null)
            return "";

        int iMax = a.length - 1;
        if (iMax == -1)
            return "[]";

        StringBuilder b = new StringBuilder();
        b.append('[');
        for (int i = 0; ; i++) {
            b.append("\"").append(a[i]).append("\"");
            if (i == iMax)
                return b.append(']').toString();
            b.append(",");
        }
    }
}
