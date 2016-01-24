package com.ec.singleOut.dao;

import com.ec.singleOut.entity.CrmDetailClassEntity;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ecuser on 2015/12/31.
 */
@Component
public class CrmDetailClassDao extends  BaseDaoSupport{

    public List<CrmDetailClassEntity> findCrmdetailClasssByCrimId(long crmId,long corpiD) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("crmId", crmId);
        map.put("corpId", corpiD);
        return getCrmSqlSession(corpiD).selectList("crmDetailClass.findCrmDetailClassByCrmId", map);
    }



}
