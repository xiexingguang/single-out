package com.ec.singleOut.dao;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ecuser on 2016/1/4.
 */
@Component
public class QqClassInfoDao extends  BaseDaoSupport {


    public void updateDecQQclassInfoByCrmIds(List<Long> crmIds) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("crmIds", crmIds);
        getBaseSqlSession("", "").delete("qqClassInfo.deleteQQinfo", map);
    }

}
