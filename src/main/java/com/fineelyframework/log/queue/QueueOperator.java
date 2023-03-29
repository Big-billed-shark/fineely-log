package com.fineelyframework.log.queue;

import com.fineelyframework.log.entity.MethodLogEntity;
import org.springframework.stereotype.Component;

import java.util.concurrent.LinkedTransferQueue;

public class QueueOperator {

    /**
     * 日志缓存队列
     */
    public static LinkedTransferQueue<MethodLogEntity> oplogQueue = new LinkedTransferQueue<>();

    private QueueOperator() {}
}
