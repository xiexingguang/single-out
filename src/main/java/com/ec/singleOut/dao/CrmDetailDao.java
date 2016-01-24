package com.ec.singleOut.dao;

import com.ec.singleOut.bean.CorpVplan;
import com.ec.singleOut.entity.CrmDetailEntity;
import com.ec.singleOut.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ecuser on 2015/12/29.
 */


@Component
public class CrmDetailDao extends  BaseDaoSupport {

    @Autowired
    private CorpVplanDao corpVplanDao;

    /**
     * 根据企业id,查询crmid，在 crm表中，是最全的crm信息了
     * @param corpid
     * @return
     */
    public List<CrmDetailEntity> findCrmDetailsByCorpId(long corpid) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("corpId", corpid);
        return getCrmSqlSession(corpid).selectList("crmDetail.findCrmDetailsByCorpId", map);
    }

    public CrmDetailEntity findCrmDetailByCorpIdAndCrmId(long corpid, long crmId) {

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("corpId", corpid);
        map.put("crmId", crmId);
        return getCrmSqlSession(corpid).selectOne("crmDetail.findCrmDetailByCorpIdAndCrmId", map);
    }


    /**
     * 更新客户资料表，更新为无人跟进,需要判断是否是最新企业
     * @param corpId
     * @param crmids
     */
    public void updateCrmDetailByCorpId(long corpId, List<Long> crmids) {

        //新企业
        boolean isNewCorp = isNewCorp(corpId);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("corpId", corpId);
        map.put("crmIds", crmids);
        map.put("contactTime", DateUtil.convertDate2String(new Date()));
        if (!isNewCorp) {
            map.put("isNew", "false");
        }
        getCrmSqlSession(corpId).update("crmDetail.updateCrmDetailsByCrmIds", map);
    }

    public boolean isNewCorp(long corpId) {
        CorpVplan corpVplan = corpVplanDao.getByCorpId(corpId);
        // 有值就是新版
        if (corpVplan != null) {
            return true;
        } else {
            return false;
        }
    }


}
