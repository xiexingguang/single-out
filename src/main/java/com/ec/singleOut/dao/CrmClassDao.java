package com.ec.singleOut.dao;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ecuser on 2016/1/8.
 */
@Component
public class CrmClassDao extends  BaseDaoSupport {

    public List<Integer> findCorpClassIdByCorpId(long corpId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("corpId", corpId);
        return getCrmSqlSession(corpId).selectList("crmClass.findCrmClassByCorpId", map);
    }


}
