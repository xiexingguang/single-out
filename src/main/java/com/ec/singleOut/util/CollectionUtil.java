package com.ec.singleOut.util;

import com.alibaba.fastjson.JSON;
import com.ec.singleOut.bean.Crmclass;
import com.ec.singleOut.entity.CrmContactTimeEntity;
import com.ec.singleOut.entity.CrmDetailEntity;
import com.ec.singleOut.entity.CrmchangeLogEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jasshine_xxg on 2016/1/2.
 */
public class CollectionUtil {

    private final static Logger LOG = LogManager.getLogger(CollectionUtil.class);
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

        Map<Long, CrmDetailEntity> maps = new HashMap<>();
        List<CrmDetailEntity> crmDetailEntities1 = new ArrayList<CrmDetailEntity>();
        for (CrmDetailEntity crmDetailEntity : crmDetailEntities) {
            maps.put(crmDetailEntity.getF_crm_id(), crmDetailEntity);
        }
        for (CrmContactTimeEntity crmContactTimeEntity : crmContactTimeEntities) {
           /* LOG.info("=======come in inCrmDetailNotIncontactTime=  for 2===========");
            long contactCrmId = crmContactTimeEntity.getF_crm_id();
            if (crmId == contactCrmId && !crmDetailEntities1.contains(crmDetailEntity)) {
                LOG.info("=======come in inCrmDetailNotIncontactTime=  for if===========");
                crmDetailEntities1.add(crmDetailEntity);
                break;
            }*/
            long crmid = crmContactTimeEntity.getF_crm_id();
            if (maps.containsKey(crmid)) {
                crmDetailEntities1.add(maps.get(crmid));
            }
        }


     /*   LOG.info("=======come in inCrmDetailNotIncontactTime============");
        List<CrmDetailEntity> crmDetailEntities1 = new ArrayList<CrmDetailEntity>();
        for (CrmDetailEntity crmDetailEntity : crmDetailEntities) {
            long crmId = crmDetailEntity.getF_crm_id();
            LOG.info("=======come in inCrmDetailNotIncontactTime=  for 1===========");
            for (CrmContactTimeEntity crmContactTimeEntity : crmContactTimeEntities) {
                LOG.info("=======come in inCrmDetailNotIncontactTime=  for 2===========");
                long contactCrmId = crmContactTimeEntity.getF_crm_id();
                if (crmId == contactCrmId && !crmDetailEntities1.contains(crmDetailEntity)) {
                    LOG.info("=======come in inCrmDetailNotIncontactTime=  for if===========");
                    crmDetailEntities1.add(crmDetailEntity);
                    break;
                }
            }
        }*/
        LOG.info("=======go out come in inCrmDetailNotIncontactTime============");
        crmDetailEntities.removeAll(crmDetailEntities1);
        LOG.info("=======after remove go out come in inCrmDetailNotIncontactTime============");
        return crmDetailEntities;
    }

    /**
     *  判断标签 是否在企业标签下
     * @param tags  规则下的标签
     * @param crms 企业标签
     * @return
     */
    public static String[] filterClassTags(String[] tags,List<Crmclass> crms) {
        List<String> tagLists = new ArrayList<>();
        for (String tag : tags) {
            if (tag.equalsIgnoreCase("0")) {
                tagLists.add(tag);
                continue;
            }
            for (Crmclass crmclass : crms) {
                long classId = crmclass.getClassId();
                String stringClassId = classId + "";
                if (tag.equalsIgnoreCase(stringClassId)) {
                    tagLists.add(tag);
                    break;
                }
            }
        }
        System.out.println(JSON.toJSON(tagLists));
        return (String[]) tagLists.toArray(new String[tagLists.size()]);
    }


    public static void main(String[] args) {
        String[] tags = {"2", "4", "5","0"};
        List<Crmclass> crms = new ArrayList<>();

      /*  crms.add(new Crmclass().setClassId(2));
        crms.add(new Crmclass().setClassId(4));
        crms.add(new Crmclass().setClassId(8));
        crms.add(new Crmclass().setClassId(154));*/

        System.out.println(filterClassTags(tags,crms));
    }


}
