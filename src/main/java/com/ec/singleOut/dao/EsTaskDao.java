/**
 * @Package com.ec.nsqc.dao.impl
 * @Description: TODO
 * @author Comsys-ecuser
 * @date 2015年7月24日 上午10:39:02
 */

package com.ec.singleOut.dao;

import com.ec.singleOut.bean.EsTask;
import org.springframework.stereotype.Repository;

import java.util.List;



/**
 * @ClassName: EsTaskDaoImpl
 * @Description: TODO
 * @author Comsys-ecuser
 * @date 2015年7月24日 上午10:39:02
 */
@Repository
public class EsTaskDao extends BaseDaoSupport {

    private static final String TABLE_NAME = "t_es_task";
    private static final String SAVE_ES_TASK = "com.ec.singleOut.bean.EsTask.saveEsTask";
    private static final String BATCH_SAVE_ES_TASK = "com.ec.singleOut.bean.EsTask.batchSaveEsTask";

    /*
     * <p>Title: saveEsTask</p> <p>Description: </p>
     * 
     * @param esTask
     * 
     * @see com.ec.nsqc.dao.EsTaskDao#saveEsTask(com.ec.nsqc.bean.EsTask)
     */


    public void saveEsTask(EsTask esTask) {
        getBaseSqlSession(UPDATE, TABLE_NAME).insert(SAVE_ES_TASK, esTask);
    }
    

    public void saveEsTask(List<EsTask> esTasks) {
        getBaseSqlSession(UPDATE, TABLE_NAME).insert(BATCH_SAVE_ES_TASK, esTasks);
    }
}
