package com.fineelyframework.log.annotation;

import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.annotation.*;

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FineelyLog {

    /**
     * 请求方法
     */
    RequestMethod[] method() default {};

    /**
     * 模块
     */
    String module() default "";

    /**
     * 描述
     * 参数名称 ${name} 或 ${class.name}
     * 常用参数如下：
     * 方法返回结果 ${result.**}
     * 方法名 ${methodName}
     * 方法开始时间 ${startTime}
     * 方法结束时间 ${endTime}
     * 求情参数 ${request.**}
     * 数组匹配 如: ${result.data[0].name}
     */
    String desc() default "";

    /**
     * 请求地址
     */
    String url() default "";
}
