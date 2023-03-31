package com.fineelyframework.log.queue;

import com.fineelyframework.log.entity.MethodLogEntity;
import org.springframework.stereotype.Component;

import java.util.concurrent.LinkedTransferQueue;

/**
 * queue operator
 */
public enum QueueOperator {
    INSTANCE;

    private LinkedTransferQueue<MethodLogEntity> oplogQueue;

    QueueOperator() {
        oplogQueue = new LinkedTransferQueue<>();
    }
    public LinkedTransferQueue<MethodLogEntity> getOplogQueue() {
        return oplogQueue;
    }

}
