package com.ec.singleOut.dao;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ecuser on 2016/1/4.
 */
@Component
public class CrmPlanDetailDao extends BaseDaoSupport {


    /**
     * 清除销售计划
     * @param corpId
     * @param crmIds
     */
    public void updateCrmPlan(long corpId, List<Long> crmIds) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("corpId", corpId);
        map.put("crmIds", crmIds);
        getCrmSqlSession(corpId).update("crmPlanDetail.updateCrmPlanDetail", map);
    }

    public void deleteCrmPlan(long corpId, List<Long> crmIds) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("corpId", corpId);
        map.put("crmIds", crmIds);
        getCrmSqlSession(corpId).delete("crmPlanDetail.deletCrmPlanDetail", map);
    }


}
