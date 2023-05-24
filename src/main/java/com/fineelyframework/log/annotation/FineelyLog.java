package com.fineelyframework.log.annotation;

import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.annotation.*;

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FineelyLog {

    RequestMethod[] method() default {};

    String module() default "";

    /**
     * Description
     * Parameter Name ${name} or ${class.name}
     * Common parameters are as follows:
     *
     * Method returns a result: ${result.**}
     * Method name: ${methodName}
     * Method execution start time: ${startTime}
     * Method execution end time: ${endTime}
     * Courtship parameter: ${request.**}
     * Array matching: ${result.data[0].name}
     * @return string
     */
    String desc() default "";

    String url() default "";
}
