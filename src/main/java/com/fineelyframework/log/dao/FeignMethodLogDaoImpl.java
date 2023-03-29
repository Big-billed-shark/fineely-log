package com.fineelyframework.log.dao;

import com.fineelyframework.log.entity.MethodLogEntity;

import javax.annotation.Resource;

public class FeignMethodLogDaoImpl implements MethodLogDao {

    @Resource
    private FineelyLogFeign fineelyLogFeign;

    @Override
    public boolean saveLog(MethodLogEntity methodLogEntity) {
        return fineelyLogFeign.save(methodLogEntity);
    }

}
