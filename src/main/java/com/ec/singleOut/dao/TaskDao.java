package com.ec.singleOut.dao;

import com.ec.singleOut.entity.TaskEntity;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ecuser on 2016/1/3.
 */
@Component
public class TaskDao extends  BaseDaoSupport {

    public void updateTask(TaskEntity taskEntity) {
        String taskPath = taskEntity.getTaskPath();
        Date taskLastEndDate = taskEntity.getLastEndDate();
        String stat = taskEntity.getStarting();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("state", stat);
        map.put("taskPath", taskPath);
        map.put("lastEndDate", taskLastEndDate);
        getSqlSessionStatic().update("task.updateTask", map);
    }


}
