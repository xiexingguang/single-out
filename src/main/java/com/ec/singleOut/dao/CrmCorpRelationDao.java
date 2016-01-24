package com.ec.singleOut.dao;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ecuser on 2016/1/4.
 */
@Component
public class CrmCorpRelationDao extends  BaseDaoSupport {


    public void deleteCrmCoprRelationShip(long corpId, List<Long> crmIds) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("corpId", corpId);
        map.put("crmIds", crmIds);
        getCrmSqlSession(corpId).update("crmCorpRelation.deleteCrmCorpRelation", map);

    }


}
