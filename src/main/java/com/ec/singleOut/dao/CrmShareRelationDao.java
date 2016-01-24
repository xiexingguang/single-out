package com.ec.singleOut.dao;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ecuser on 2015/12/29.
 */
@Component
public class CrmShareRelationDao extends  BaseDaoSupport{


    public void deleteCrmShareRelation(long corpID, List<Long> crmids) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("corpId", corpID);
        map.put("crmIds", crmids);
        getCrmSqlSession(corpID).delete("crmShareRelation.deleteCrmShareRelation", map);
    }

}
