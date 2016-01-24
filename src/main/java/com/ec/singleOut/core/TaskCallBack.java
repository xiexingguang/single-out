package com.ec.singleOut.core;

import com.ec.singleOut.entity.TaskEntity;

/**
 * Created by jasshine_xxg on 2016/1/2.
 */
public interface TaskCallBack {

    void notifyTaskTheResultOfcallService(TaskEntity entity);
}
