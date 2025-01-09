package com.fineelyframework.log.dao;

import com.alibaba.fastjson2.JSONObject;
import com.fineelyframework.log.config.FineelyKafkaConfig;
import com.fineelyframework.log.entity.MethodLogEntity;
import org.springframework.kafka.core.KafkaTemplate;

import jakarta.annotation.Resource;

public class KafkaMethodLogDaoImpl implements MethodLogDao {

    @Resource
    private KafkaTemplate<String, String> kafkaClient;

    @Resource
    private FineelyKafkaConfig fineelyKafkaConfig;

    @Override
    public boolean saveLog(MethodLogEntity methodLogEntity) {
        kafkaClient.send(fineelyKafkaConfig.getTopic(), JSONObject.toJSONString(methodLogEntity));
        return true;
    }

}
