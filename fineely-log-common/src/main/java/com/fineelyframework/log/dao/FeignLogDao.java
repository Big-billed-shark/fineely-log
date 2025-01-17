package com.fineelyframework.log.dao;

import com.fineelyframework.log.entity.MethodLogEntity;

public interface FeignLogDao {

    boolean saveLog(MethodLogEntity methodLogEntity);
}
