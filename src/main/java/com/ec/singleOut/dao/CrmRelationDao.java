package com.ec.singleOut.dao;

import com.ec.singleOut.entity.CrmDetailEntity;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ecuser on 2015/12/29.
 */
@Component
public class CrmRelationDao extends BaseDaoSupport{


    public void updateCrmRleationByCorpId(long corpId, List<Long> crmids) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("corpId", corpId);
        map.put("crmIds", crmids);
        getCrmSqlSession(corpId).update("crmRelation.updateCrmRelation", map);
    }

    /**
     * 查询没有设置标签的客户id
     * @param corpId
     * @param crmids
     * @return
     */
    public List<Long> searcherCrmIdHasNoSetTag(long corpId, List<CrmDetailEntity> crmids) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("corpId", corpId);
        map.put("crmIds", crmids);
        return getCrmSqlSession(corpId).selectList("crmRelation.selectCrmNotSetTag", map);
    }


}
