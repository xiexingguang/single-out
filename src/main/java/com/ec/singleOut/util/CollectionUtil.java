package com.ec.singleOut.util;

import com.alibaba.fastjson.JSON;
import com.ec.singleOut.entity.CrmContactTimeEntity;
import com.ec.singleOut.entity.CrmDetailEntity;
import com.ec.singleOut.entity.CrmchangeLogEntity;

import java.util.*;

/**
 * Created by jasshine_xxg on 2016/1/2.
 */
public class CollectionUtil {


    public static List<Long> convertListCrmDetail2Long(List<CrmDetailEntity> crmDetailEntities) {
        List<Long> longsCrm = new ArrayList<Long>();
        for (CrmDetailEntity crmDetailEntity : crmDetailEntities) {
            longsCrm.add(crmDetailEntity.getF_crm_id());
        }
        return longsCrm;
    }

    /**
     *
     * @param crmids  该企业下crm 的子集
     * @param crmDetailEntities ，该企业下crmdetail 的全集
     * @return
     */
    public static List<CrmDetailEntity> convertLong2CrmDetail(List<Long> crmids,List<CrmDetailEntity> crmDetailEntities) {
        List<CrmDetailEntity> long2crm = new ArrayList<CrmDetailEntity>();
        for (CrmDetailEntity crmDetailEntity : crmDetailEntities) {
            for (Long crmid : crmids) {
                if (crmDetailEntity.getF_crm_id() == crmid) {
                    long2crm.add(crmDetailEntity);
                }
            }
        }
        return long2crm;
    }

    public static List<CrmchangeLogEntity> convertLong2CrmChangeLog(List<Long> crmids,List<CrmDetailEntity> crmDetailEntities) {
        List<CrmchangeLogEntity> crmchangeLogEntities = new ArrayList<CrmchangeLogEntity>();
        List<CrmDetailEntity> crmDetails = convertLong2CrmDetail(crmids,crmDetailEntities); // 先转成成crmDetail对象
        for (CrmDetailEntity crmDetailEntity : crmDetails) {
            CrmchangeLogEntity crmchangeLogEntity = new CrmchangeLogEntity();
            crmchangeLogEntity.setF_act_userid(crmDetailEntity.getF_user_id());
            crmchangeLogEntity.setF_corp_id(crmDetailEntity.getF_corp_id());
            crmchangeLogEntity.setF_crm_id(crmDetailEntity.getF_crm_id());
            crmchangeLogEntity.setF_user_id(crmDetailEntity.getF_user_id());
            crmchangeLogEntity.setF_memo("掉单删除");
            crmchangeLogEntities.add(crmchangeLogEntity);
        }
        return crmchangeLogEntities;
    }


    /**
     * 筛选掉单的crmIds，是否在掉单列表中
     * 每次执行定时清理任务前，会重新获得一次掉单列表。
     * 判断执行的crmid 是否在掉单列表中
     * 因为有可能在执行清理时候，从队列中取出的crmiDs已经脱单了。
     * @param crmDetailEntities
     * @param crmIds
     * @return
     */
    public static List<Long> filterHasNoLoseCrmId(List<CrmDetailEntity> crmDetailEntities,List<Long> crmIds) {
        List<Long> fiterCrmId = new ArrayList<Long>();
        for (Long crmid : crmIds) {
            for (CrmDetailEntity crmDetailEntity : crmDetailEntities) {
                if (crmid == crmDetailEntity.getF_crm_id()) {
                    fiterCrmId.add(crmid);
                }
            }
        }
        return fiterCrmId;
    }


    /**
     * 将掉单集合中。生成usr_id----------->crmids
     *
     * @param crmDetailEntities
     * @return
     */
    public static Map<Long/**usr_id**/, List<CrmDetailEntity>> generateUserIdWithCrmIds(List<CrmDetailEntity> crmDetailEntities) {
        if (crmDetailEntities == null || crmDetailEntities.size() == 0) {
            return null;
        }
        Map<Long, List<CrmDetailEntity>> userCrmIdsMap = new HashMap<Long, List<CrmDetailEntity>>();
        for (CrmDetailEntity crmDetailEntity : crmDetailEntities) {
            long usrId = crmDetailEntity.getF_user_id();
            if (!userCrmIdsMap.containsKey(usrId)) {
                List<CrmDetailEntity> crmDetailEntities1 = new ArrayList<CrmDetailEntity>();
                userCrmIdsMap.put(usrId, crmDetailEntities1);
                crmDetailEntities1.add(crmDetailEntity);
            } else {
                List<CrmDetailEntity> crmDetailEntities2 = userCrmIdsMap.get(usrId);
                if (crmDetailEntities2 == null) {
                    crmDetailEntities2 = new ArrayList<CrmDetailEntity>();
                    userCrmIdsMap.put(usrId, crmDetailEntities2);
                }
                crmDetailEntities2.add(crmDetailEntity);
            }
        }
        return userCrmIdsMap;
    }



    public static List<CrmDetailEntity> inCrmDetailNotIncontactTime(List<CrmDetailEntity> crmDetailEntities, List<CrmContactTimeEntity> crmContactTimeEntities) {
        List<CrmDetailEntity> crmDetailEntities1 = new ArrayList<CrmDetailEntity>();
        for (CrmDetailEntity crmDetailEntity : crmDetailEntities) {
            long crmId = crmDetailEntity.getF_crm_id();
            for (CrmContactTimeEntity crmContactTimeEntity : crmContactTimeEntities) {
                long contactCrmId = crmContactTimeEntity.getF_crm_id();
                if (crmId == contactCrmId && !crmDetailEntities1.contains(crmDetailEntity)) {
                    crmDetailEntities1.add(crmDetailEntity);
                    break;
                }
            }
        }
        crmDetailEntities.removeAll(crmDetailEntities1);
        return crmDetailEntities;
    }




    public static void main(String[] args) {
        CrmDetailEntity crmDetailEntity = new CrmDetailEntity();
        crmDetailEntity.setF_corp_id(20);
        crmDetailEntity.setF_crm_id(20);

        CrmDetailEntity crmDetailEntity1 = new CrmDetailEntity();
        crmDetailEntity1.setF_corp_id(30);
        crmDetailEntity1.setF_crm_id(30);

        CrmDetailEntity crmDetailEntity2 = new CrmDetailEntity();
        crmDetailEntity2.setF_corp_id(40);
        crmDetailEntity2.setF_crm_id(40);


        CrmDetailEntity crmDetailEntity3 = new CrmDetailEntity();
        crmDetailEntity.setF_corp_id(20);
        crmDetailEntity.setF_crm_id(20);

        List<CrmDetailEntity> crmDetailEntities = new ArrayList<CrmDetailEntity>();
        crmDetailEntities.add(crmDetailEntity);
        crmDetailEntities.add(crmDetailEntity1);
        crmDetailEntities.add(crmDetailEntity2);

        List<CrmContactTimeEntity> crmDetailEntities2 = new ArrayList<CrmContactTimeEntity>();
        CrmContactTimeEntity crmContactTimeEntity = new CrmContactTimeEntity();
        crmContactTimeEntity.setF_crm_id(20);
        CrmContactTimeEntity crmContactTimeEntity2 = new CrmContactTimeEntity();
        crmContactTimeEntity2.setF_crm_id(60);
        crmDetailEntities2.add(crmContactTimeEntity);
        crmDetailEntities2.add(crmContactTimeEntity2);


        System.out.println(JSON.toJSON(inCrmDetailNotIncontactTime(crmDetailEntities, crmDetailEntities2)));


    }


}
