package com.fineelyframework.log.dao;

import com.fineelyframework.log.entity.MethodLogEntity;

public interface MethodLogDao {

    /**
     * 保存接口监控日志
     */
     boolean saveLog(MethodLogEntity methodLogEntity);

}
