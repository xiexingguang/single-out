package com.ec.singleOut.dao;

import com.ec.singleOut.entity.CrmLoseRuleEntity;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ecuser on 2015/12/28.
 */
@Component
public class CrmLoseRuleDao extends  BaseDaoSupport{

    /**
     * 根据企业id查询掉单规则
     * @param corpId
     * @return
     */
    public CrmLoseRuleEntity searcherRulesByCorpId(long corpId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("corpId", corpId);
        return getCrmSqlSession(corpId).selectOne("loseRule.getLoseRuleByCoprId", map);
    }

    /**
     * 从crm0中查询所有设置了掉单规则的企业id
     * @return
     */
    public List<CrmLoseRuleEntity> searcherRulesFromCrm0() {

        return getSqlSessionCrm0().selectList("loseRule.getLoseRule");
    }

    public List<CrmLoseRuleEntity> searcherRulesFromCrm1() {

        return getSqlSessionCrm1().selectList("loseRule.getLoseRule");
    }

    public List<CrmLoseRuleEntity> searcherRulesFromCrm2() {

        return getSqlSessionCrm2().selectList("loseRule.getLoseRule");
    }

    public List<CrmLoseRuleEntity> searcherRulesFromCrm3() {

        return getSqlSessionCrm3().selectList("loseRule.getLoseRule");
    }



}
