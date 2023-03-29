package com.fineelyframework.log;

import com.alibaba.fastjson2.JSONObject;
import com.fineelyframework.log.entity.MethodLogEntity;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.bind.annotation.RequestMethod;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

class FineelyLogApplicationTests {

    public static void main(String[] args) {
        MethodLogEntity methodLogEntity = new MethodLogEntity();
        methodLogEntity.setOperator("system");
        methodLogEntity.setMethodName("name");
        methodLogEntity.setAllParams("123,123");
        methodLogEntity.setMethod(new RequestMethod[]{RequestMethod.GET, RequestMethod.DELETE});
        methodLogEntity.setModule("test");
        methodLogEntity.setUrl("https://dasdadasd");
        methodLogEntity.setDesc("what are you doing");
        methodLogEntity.setStartTime(LocalDateTime.now());
        methodLogEntity.setEndTime(LocalDateTime.now());
        methodLogEntity.setTimeConsuming(0.1);
        methodLogEntity.setResult("123213");
        methodLogEntity.setIpAddress("192.168.3.3");
        methodLogEntity.setExceptionInfo(null);
        methodLogEntity.setCreateTime(LocalDateTime.now());
        System.out.println(JSONObject.toJSONString(methodLogEntity));
        System.out.println(methodLogEntity.toString());
    }

}
