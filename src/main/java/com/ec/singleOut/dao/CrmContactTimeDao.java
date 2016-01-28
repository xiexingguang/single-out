package com.ec.singleOut.dao;

import com.alibaba.fastjson.JSON;
import com.ec.singleOut.entity.CrmContactTimeEntity;
import com.ec.singleOut.util.DbUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ecuser on 2015/12/29.
 */
@Component
public class CrmContactTimeDao extends  BaseDaoSupport {

    private final Logger LOG = LogManager.getLogger(CrmContactTimeDao.class);

    /**
     * 查询某个企业下的crmids,这些crmids的操作类型都符合有效联系的类型
     * 因为该表里的数据crmid，有多个，由于根据业务，我们只需要取crmid记录中
     * contact_time字段最新的记录即可
     * 解决方案：http://www.qmtx3.com/pc/web/201404/35481.html
     * @param coprid
     * @param effectiveType ：有效类型
     * @return
     * 在sql层面解决，不需要load到内存，在进行去重排序了，取时间最新的
     */
    public List<CrmContactTimeEntity> findCrmOpearationTypeInEffecitiveCallWays(long coprid, String effectiveType) {
        /**
         select * from t_crm_contact_time_04 where f_contact_time in
         (select max(f_contact_time) from t_crm_contact_time_04 GROUP BY f_crm_id )
         and f_type=2 ORDER BY f_crm_id
         */

        final String tableName = DbUtil.getContactTimeTableName(coprid, 20);
        Map<String, Object> map = new HashMap<String, Object>();
        LOG.info("contactTime tableName ===============> " + tableName);
        map.put("tableName", tableName);
        map.put("corpId", coprid);
        map.put("type", effectiveType);
        List<CrmContactTimeEntity> crmContactTimeEntities  = getCrmSqlSession(coprid).selectList("contactTime.findNewestCrmContactTime",map);
        LOG.info("crmContactTimeEnity ===="+crmContactTimeEntities.size());
        return crmContactTimeEntities;
    }


    public List<CrmContactTimeEntity> findCrmOpearationTypeNotInEffecitiveCallWays(long coprid, String effectiveType) {
        /**
         select * from t_crm_contact_time_04 where f_contact_time in
         (select max(f_contact_time) from t_crm_contact_time_04 GROUP BY f_crm_id )
         and f_type=2 ORDER BY f_crm_id
         */

        final String tableName = DbUtil.getContactTimeTableName(coprid, 20);
        Map<String, Object> map = new HashMap<String, Object>();
        LOG.info("contactTime tableName ===============> " + tableName);
        map.put("tableName", tableName);
        map.put("corpId", coprid);
        map.put("type", effectiveType);
        List<CrmContactTimeEntity> crmContactTimeEntities  = getCrmSqlSession(coprid).selectList("contactTime.findNotNewestCrmContactTime",map);
        LOG.debug("crmContactTimeEnity ===="+JSON.toJSON(crmContactTimeEntities));
        return crmContactTimeEntities;
    }

}
