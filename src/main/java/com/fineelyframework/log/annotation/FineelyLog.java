package com.fineelyframework.log.annotation;

import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.annotation.*;

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FineelyLog {

    RequestMethod[] method() default {};

    String module() default "";

    String desc() default "";

    String url() default "";
}
