package com.fineelyframework.log.dao;

import com.fineelyframework.log.entity.MethodLogEntity;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
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
