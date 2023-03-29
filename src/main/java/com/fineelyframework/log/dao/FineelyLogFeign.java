package com.fineelyframework.log.dao;

import com.fineelyframework.log.entity.MethodLogEntity;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@ConditionalOnProperty(prefix = "fineely.log", name = "storage-mode", havingValue = "feign")
@FeignClient(name = "${fineely.log.feign.name}", path = "${fineely.log.feign.path}", url = "${fineely.log.feign.url:}")
public interface FineelyLogFeign {

    @PostMapping(value = "/saveAll")
    boolean saveAll(@RequestBody List<MethodLogEntity> methodLogEntities);

    @PostMapping(value = "/save")
    boolean save(@RequestBody MethodLogEntity methodLogEntity);

}
