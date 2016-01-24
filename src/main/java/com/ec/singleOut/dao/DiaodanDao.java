package com.ec.singleOut.dao;

import com.ec.singleOut.entity.LoseRecordEntity;
import com.ec.singleOut.util.DbUtil;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ecuser on 2015/12/28.
 */
@Component
public class DiaodanDao extends BaseDaoSupport{


    /**
     *
     * @param loseRecordEntities
     * @param corpId
     */
    public void updateLoseRecord(List<LoseRecordEntity> loseRecordEntities,long corpId) {
        final String tableName = DbUtil.getLoseRecordTableName(corpId, 20);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("corpId", corpId);
        map.put("tableName", tableName);
        map.put("loseRecords", loseRecordEntities);
        if (loseRecordEntities.size() == 0) {
            return;
        }
        System.out.println("====================>lose record NAME ====."+tableName);
        getCrmSqlSession(corpId).update("loseRecordCrm.updateLoseRecordCrm", map);
    }

}
