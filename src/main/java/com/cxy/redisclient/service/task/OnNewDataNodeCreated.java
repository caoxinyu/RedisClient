package com.cxy.redisclient.service.task;

import com.cxy.redisclient.domain.DataNode;

import java.util.Set;

/**
 * Created by zj122708 on 2016/11/26.
 */
public interface OnNewDataNodeCreated {
    void onNewDataNodeCreated(Set<DataNode> dataNodeSet);
}
