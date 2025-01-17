package com.fineelyframework.log.dao;

import com.fineelyframework.log.entity.MethodLogEntity;
import org.apache.dubbo.config.annotation.DubboReference;

public class DubboMethodLogDaoImpl implements MethodLogDao {

    @DubboReference
    private FeignLogDao feignLogDao;

    @Override
    public boolean saveLog(MethodLogEntity methodLogEntity) {
        return feignLogDao.saveLog(methodLogEntity);
    }

}
