package com.cxy.redisclient.service.task;

import com.cxy.redisclient.dto.Order;
import com.cxy.redisclient.dto.OrderBy;
import com.cxy.redisclient.integration.JedisCommand;
import com.cxy.redisclient.integration.key.ListContainerKeys;

/**
 * Created by zj122708 on 2016/11/26.
 */
public class AsynchronousTask extends Thread {
    ListContainerKeys command;
    public AsynchronousTask(int id, int db, String key, boolean flat, Order order, OrderBy orderBy,
                            OnNewDataNodeCreated onNewDataNodeCreated) {
        command = new ListContainerKeys(id, db, key, flat, order, orderBy);
        command.setOnNewDataNodeCreated(onNewDataNodeCreated);
    }

    @Override
    public void run() {
        super.run();
        command.execute();
    }
}
