package com.ec.singleOut.dao;

import com.ec.singleOut.entity.NsqTaskEntity;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by ecuser on 2015/12/30.
 */
@Component
public class NsqTaskDao extends  BaseDaoSupport {


    public void batchSaveNsqTaskDao(List<NsqTaskEntity> nsqTaskEntities) {
        getBaseSqlSession(UPDATE, "t_nsq_task").insert("nsqTask.batchSave", nsqTaskEntities);
    }

}
