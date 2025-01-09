package com.fineelyframework.log.dao;

import com.fineelyframework.log.entity.MethodLogEntity;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;
import java.util.List;

@Component
@ConditionalOnProperty(prefix = "fineely.log", name = "storage-mode", havingValue = "feign")
public class FineelyLogFeignImpl implements FineelyLogFeign {


    @Override
    public boolean saveAll(List<MethodLogEntity> methodLogEntities) {
        return false;
    }

    @Override
    public boolean save(MethodLogEntity methodLogEntity) {
        return false;
    }
}
