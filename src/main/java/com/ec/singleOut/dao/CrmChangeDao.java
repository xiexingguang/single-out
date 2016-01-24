package com.ec.singleOut.dao;

import com.ec.singleOut.entity.CrmchangeLogEntity;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ecuser on 2016/1/6.
 */
@Component
public class CrmChangeDao extends BaseDaoSupport{


    public void updateCrmChange(long corpid, List<CrmchangeLogEntity> crmchangeLogEntities) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("corpId", corpid);
        map.put("changeLogList", crmchangeLogEntities);
        getCrmSqlSession(corpid).update("crmChange.updateChange", map);
    }

}
