package com.ec.singleOut.dao;

import com.ec.singleOut.entity.LoseRecordEntity;
import com.ec.singleOut.util.DbUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ecuser on 2015/12/28.
 */
@Component
public class DiaodanDao extends BaseDaoSupport{

    private final Logger LOG = LogManager.getLogger(DiaodanDao.class);
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
        LOG.info("====:lose record NAME ====."+tableName);
        getCrmSqlSession(corpId).update("loseRecordCrm.updateLoseRecordCrm", map);
    }




    public void truncateCrm0LoseReocrd() {
        for (int i = 0; i < 20; i++) {
            String tableName ;
            if (i < 10) {
                tableName = "d_ec_crmextend.t_crm_lose_record_0" +i;
            } else {
                tableName = "d_ec_crmextend.t_crm_lose_record_" +i;
            }
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("tableName", tableName);
            getSqlSessionCrm0().delete("loseRecordCrm.truncateLoseRecordCrm", map);
        }
    }

    public void truncateCrm1LoseReocrd(){
        for (int i = 0; i < 20; i++) {
            String tableName ;
            if (i < 10) {
                tableName = "d_ec_crmextend.t_crm_lose_record_0" +i;
            } else {
                tableName = "d_ec_crmextend.t_crm_lose_record_" +i;
            }
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("tableName", tableName);
            System.out.println("name===========>"+tableName);
            getSqlSessionCrm1().delete("loseRecordCrm.truncateLoseRecordCrm", map);
        }
    }

    public void truncateCrm2LoseReocrd(){

        for (int i = 0; i < 20; i++) {
            String tableName ;
            if (i < 10) {
                tableName = "d_ec_crmextend.t_crm_lose_record_0" +i;
            } else {
                tableName = "d_ec_crmextend.t_crm_lose_record_" +i;
            }
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("tableName", tableName);
            getSqlSessionCrm2().delete("loseRecordCrm.truncateLoseRecordCrm", map);
        }
    }
    public void truncateCrm3LoseReocrd(){
        for (int i = 0; i < 20; i++) {
            String tableName ;
            if (i < 10) {
                tableName = "d_ec_crmextend.t_crm_lose_record_0" +i;
            } else {
                tableName = "d_ec_crmextend.t_crm_lose_record_" +i;
            }
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("tableName", tableName);
            getSqlSessionCrm3().delete("loseRecordCrm.truncateLoseRecordCrm", map);
        }
    }
}
