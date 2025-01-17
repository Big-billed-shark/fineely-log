package com.fineelyframework.log.config;

import com.fineelyframework.log.aspect.DefaultMethodLogHandler;
import com.fineelyframework.log.aspect.MethodLogHandler;
import com.fineelyframework.log.dao.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * fineely配置类
 */
@Configuration
@ConfigurationProperties(prefix = "fineely.log")
public class FineelyConfig {

    private String storageMode = "default";

    @Bean
    @ConditionalOnMissingBean
    public MethodLogHandler methodLogHandler() {
        return new DefaultMethodLogHandler();
    }

    @Bean
    @ConditionalOnMissingBean
    public MethodLogDao methodLogDao() {
        switch (storageMode) {
            case "feign":
                return new FeignMethodLogDaoImpl();
            case "kafka":
                return new KafkaMethodLogDaoImpl();
            case "dubbo":
                return new DubboMethodLogDaoImpl();
            default:
                return new QueueMethodLogDaoImpl();
        }
    }

    public String getStorageMode() {
        return storageMode;
    }

    public void setStorageMode(String storageMode) {
        this.storageMode = storageMode;
    }

}
