package com.ec.singleOut.service.impl;
import com.alibaba.fastjson.JSON;
import com.ec.singleOut.Constants.DiaodanConstants;
import com.ec.singleOut.bean.LoseCrmId;
import com.ec.singleOut.core.AbstractPagingHandler;
import com.ec.singleOut.core.SingleOutContext;
import com.ec.singleOut.core.TaskCallBack;
import com.ec.singleOut.dao.*;
import com.ec.singleOut.entity.*;
import com.ec.singleOut.json.JsonUtil;
import com.ec.singleOut.json.WriteNsqJson;
import com.ec.singleOut.nsq.NsqHandler;
import com.ec.singleOut.properties.ConfigProperties;
import com.ec.singleOut.redis.RedisTemplate;
import com.ec.singleOut.service.DiaodanService;
import com.ec.singleOut.thrift.api.EsProxyService;
import com.ec.singleOut.thrift.client.ThriftClient;
import com.ec.singleOut.util.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.thrift.TException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by xxg on 2015/12/28.
 */
@Component("singleOUtService")
public class DiaodanServiceImpl implements DiaodanService {

    private final Logger LOG = LogManager.getLogger(DiaodanServiceImpl.class);

    @Autowired
    private CrmLoseRuleDao crmLoseRuleDao;
    @Autowired
    private CrmContactTimeDao crmContactTimeDao;


    @Autowired
    private CrmDetailDao crmDetailDao;   //
    @Autowired
    private CrmRelationDao crmRelationDao;
    @Autowired
    private CrmShareRelationDao crmShareRelationDao;
    @Autowired
    private CrmCorpRelationDao crmCorpRelationDao;
    @Autowired
    private QqClassInfoDao qqClassInfoDao;
    @Autowired
    private CrmPlanDetailDao crmPlanDetailDao;
    @Autowired
    private CrmChangeDao crmChangeDao;
    @Autowired
    private CrmChangeLogDao crmChangeLogDao;
    @Autowired
    private CrmChangeOnceDao crmChangeOnceDao;
    @Autowired
    private MemcachedDao memcachedDao;
    @Autowired
    private CrmDetailClassDao crmDetailClassDao;

    @Autowired
    private RedisTemplate redisTemplate;


    @Autowired
    private TaskDao taskDao;
    @Autowired
    private DiaodanDao diaodanDao;


    @Autowired
    private NsqHandler nsqHandler;
    @Autowired
    private ThriftClient thriftClient;
    @Autowired
    private ConfigProperties configProperties;


    private EsProxyService.Client client;

    public CrmLoseRuleEntity searcherRulesByCorpId(long corpId) {
        return null;
    }


    /**
     * 在多个crm分库中，寻找set 掉单的企业ids
     * @return
     */
    public List<CrmLoseRuleEntity> searcherSetDiaodanCorpIds() {
        LOG.info("begin to deal with diaodan service ");
        //查询设置过掉单规则的corpids,
        List<CrmLoseRuleEntity> totalcCoprids = new ArrayList<CrmLoseRuleEntity>();

        //get lose corpId from other crm  reposity
        LOG.info("scan crm reposity ,find lose dan corpIDs");
        List<CrmLoseRuleEntity> crm0 = null;
        List<CrmLoseRuleEntity> crm1 = null;
        List<CrmLoseRuleEntity> crm2 = null;
        List<CrmLoseRuleEntity> crm3 = null;

        try {
            crm0 = crmLoseRuleDao.searcherRulesFromCrm0();
            LOG.info("crm0 corpid size :" + crm0.size());
            totalcCoprids.addAll(crm0);
        } catch (Exception e) {
            LOG.error("fail to get the lose corpId from crm0 ",e);
        }

        try {
            crm1 = crmLoseRuleDao.searcherRulesFromCrm1();
            LOG.info("crm1 corpid size :" + crm1.size());
            totalcCoprids.addAll(crm1);
        } catch (Exception e) {
            LOG.error("fail to get the lose corpId from crm1 ",e);
        }

        try {
            crm2 = crmLoseRuleDao.searcherRulesFromCrm2();
            LOG.info("crm2 corpid size :" + crm2.size());
            totalcCoprids.addAll(crm2);
        } catch (Exception e) {
            LOG.error("fail to get the lose corpId from crm2 ",e);
        }

        try {
            crm3 = crmLoseRuleDao.searcherRulesFromCrm3();
            LOG.info("crm3 corpid size :" + crm3.size());
            totalcCoprids.addAll(crm3);
        } catch (Exception e) {
            LOG.error("fail to get the lose corpId from crm3 ",e);
        }
        // log
        LOG.info("total corpid size :" + totalcCoprids.size());
        return totalcCoprids;
    }


    /**
     *
     * @param  timeInterval　离掉单日期前间隔多久时间  =0 ，表示查询已经掉单的crmid，
     *                   不为0，表示查询离掉单前timeInterval时间的crmids
     * @param corpid
     * @return null. if return null ,it means argument are wrong
     */
    public List<CrmDetailEntity> searcherWillDeadLineDiaodanCrmIdByCorpId(int timeInterval,long corpid) {

        //参数判断
        if (corpid == 0 || timeInterval < 0 ) {
            throw new IllegalArgumentException("timeinterval or coprid  argument not right.");
        }
        //根据企业id，查询出该企业id的掉单规则
        CrmLoseRuleEntity crmLoseRuleEntity = crmLoseRuleDao.searcherRulesByCorpId(corpid);
        List<CrmDetailEntity> crmDetailEntities = null;

        if (crmLoseRuleEntity == null) {
            LOG.error("lose rule is not exist ,the coprid is :"+corpid);
            return crmDetailEntities;
        }
        LOG.info("the rules of corpdi:===========>"+crmLoseRuleEntity);

        final int contact1 = crmLoseRuleEntity.getF_no_contact1();  //客户在客户库里面，表示 第一次创建。这个规则，往往它的联系时间就是create_time
        final int contact2 = crmLoseRuleEntity.getF_no_contact2();
        final int updateNo = crmLoseRuleEntity.getF_no_update();
        final String contact_type = crmLoseRuleEntity.getF_contact_type();
        final String f_tag_set = crmLoseRuleEntity.getF_tag_set();
        long currentTime = System.currentTimeMillis();


        // 如果3个掉单规则为null,但是f_tag_set又有值，说明数据有问题（根据业务来讲，是不可能的）
        if (contact1 == 0 && contact2 == 0 && updateNo == 0 && f_tag_set != null) {
            LOG.error("rules are not fit for tag_set ,the wrong rules of corpid is ,"+corpid );
            return crmDetailEntities;
        }

        if (updateNo == 0 && contact_type.contains("8")) {
            LOG.error("rules are not fit for updateNo and contact_type 8 ,the wrong rules of corpid is ,"+corpid );
            return crmDetailEntities;
        }

        if (updateNo !=0 && !contact_type.contains("8")) {
            LOG.error("rules are not fit for updateNo and contact_type 8 ,the wrong rules of corpid is ,"+corpid );
            return crmDetailEntities;
        }
        //init loseCrmIDS collections
        crmDetailEntities = new ArrayList<CrmDetailEntity>(); //一个企业下，掉单的crmid集合

        // 在detail表中,不在contactTime表里的crm，并且设置了掉单规则1
        if (contact1 != 0) {
            List<CrmDetailEntity> notIncontactTime = findNotIncontactTimeTableCrmId(corpid,contact_type);
            for (CrmDetailEntity entity : notIncontactTime) {
                final String stringngCreateTime = entity.getF_create_Time();
                final String lose_time = DateUtil.addDateOfDay(stringngCreateTime, contact1);
                final long usrs_id = entity.getF_user_id();
                long day2lose = DateUtil.convertStringDate2LongDate(lose_time) - currentTime;
                //log
                LOG.info("规则1，企业id ：" + corpid  +" , 客户id为 ："+entity.getF_crm_id()+"===========> 掉单时间 ==》" + lose_time );

                // 即将掉单的
                if (timeInterval != 0) {
                    String rember_time = DateUtil.removeDateOfHour(lose_time, timeInterval);
                    long intervalRember = currentTime - DateUtil.convertStringDate2LongDate(rember_time) ; //当前时间 - 提醒时间
                    //没有掉单
                    if (day2lose > 0) {
                        if (intervalRember >= 0) {  //在规定的时间内,即提醒的时间内
                            entity.setF_lose_time(lose_time);
                            entity.setF_new_contact_time(stringngCreateTime);
                            entity.setF_user_id(usrs_id);
                            crmDetailEntities.add(entity);
                        }
                    }
                } else { //计算已经掉单的
                    if (day2lose <= 0) {
                        crmDetailEntities.add(entity);
                    }
                }
            }//end for
        }//end if

            // 如果掉单规则2，3有一个满足，则需要到contact_time表里查询
            if (contact2 != 0 || updateNo != 0) {
                //这里面查询出来的crmid,可能在
                List<CrmContactTimeEntity> crmContactTimeEntities = crmContactTimeDao.findCrmOpearationTypeInEffecitiveCallWays(corpid, contact_type);
                LOG.debug("============debugcrmContactTimeEntities============" + JSON.toJSONString(crmContactTimeEntities));
                for (int i = 0; i < crmContactTimeEntities.size(); i++) {
                    CrmContactTimeEntity crmContactTimeEntity = crmContactTimeEntities.get(i);
                    String contactTime = crmContactTimeEntity.getF_contact_time(); //客户最新操作时间
                    long crmid = crmContactTimeEntity.getF_crm_id();
                    final  int type = crmContactTimeEntity.getF_type();
                    int effective_contactTime = 0;         // 规则中，未有效联系的天数

                    if (type == DiaodanConstants.CONTATC_TYPE_UPDATECRM) { //如果是跟进客户
                        effective_contactTime = updateNo;
                    } else {
                        effective_contactTime = contact2;
                    }

                    final String lose_time = DateUtil.addDateOfDay(contactTime, effective_contactTime); // 调单时间
                    long day2lose = DateUtil.convertStringDate2LongDate(lose_time) - currentTime;

                    //log
                    LOG.info("规则1，企业id ：" + corpid  +" , 客户id为 ："+crmContactTimeEntity.getF_crm_id()+"===========> 掉单时间 ==》" + lose_time );

                    if (timeInterval != 0) {           // 查询的是即将timeInterval时间间隔掉单的crmId
                        final String rember_time = DateUtil.removeDateOfHour(lose_time, timeInterval);
                        long intervalRember = currentTime - DateUtil.convertStringDate2LongDate(rember_time) ; //当前时间 - 提醒时间
                        if (day2lose > 0) { // 表示还木有掉单
                            if (intervalRember >=  0) {  //在指定的时间间隔内
                                CrmDetailEntity crmDetailEntity = crmDetailDao.findCrmDetailByCorpIdAndCrmId(corpid, crmid); // 查询出来可能为null
                                if(crmDetailEntity != null){
                                    crmDetailEntity.setF_lose_time(lose_time);
                                    crmDetailEntity.setF_new_contact_time(contactTime);
                                    crmDetailEntity.setF_type(type);
                                    crmDetailEntity.setF_user_id(crmDetailEntity.getF_user_id());
                                    crmDetailEntities.add(crmDetailEntity);
                                }
                            }
                        }
                    } else { //查询已经掉单的
                        if (day2lose < 0) {
                            CrmDetailEntity crmDetailEntity = crmDetailDao.findCrmDetailByCorpIdAndCrmId(corpid, crmid);// 查询出来可能为null
                            if (crmDetailEntity != null) {
                                crmDetailEntities.add(crmDetailEntity);
                            }
                        }
                    }
                }
            }//end if contact2 != 0

         // 筛选数据，比如去重，以及 筛选，条件中有自定义标签的，即f_tag_set !=""的
        if (f_tag_set != null && !f_tag_set.equals("")) {
            String[] tags = f_tag_set.split(",");
            List<CrmDetailEntity> noTagsCrm = null;
            boolean isNeedAddNotagCrm = false;
            //筛选无标签的crmid
            for (String tag : tags) {
                if (tag.equals(DiaodanConstants.NO_TAG)) {  //表示标签规则中设置了无标签
                    isNeedAddNotagCrm = true;
                    if(crmDetailEntities != null && crmDetailEntities.size() > 0) {
                        List<Long> noTagsCrmLong = crmRelationDao.searcherCrmIdHasNoSetTag(corpid, crmDetailEntities); // 筛选无标签的crmid
                        noTagsCrm = CollectionUtil.convertLong2CrmDetail(noTagsCrmLong, crmDetailEntities);
                        LOG.info("cropId: " + corpid + " has contains no tag rule,no tag crmIds number  is :" + noTagsCrm.size());
                    }
                }
            }

            //标签过滤
            remvoeDuplicateCrmIdAndfilterNotContainTag(crmDetailEntities, tags);

            //rule设置了无标签客户也需要掉单
            if (isNeedAddNotagCrm && noTagsCrm != null) {
                crmDetailEntities.addAll(noTagsCrm);
            }
        }

        return crmDetailEntities;
    }


    /**
     * 一个企业企业去做掉单处理
     * 处理掉单的crmid
     * @param corpid
     */
    public void dealDeadLineDiaodanCrmIdByCorpId(long corpid) {

        //查出掉单的crmid，这个掉单的crmid，数量可能巨大
        List<CrmDetailEntity> crmDetailEntities = searcherWillDeadLineDiaodanCrmIdByCorpId(0, corpid);
        if (crmDetailEntities == null || crmDetailEntities.size() == 0) {
            LOG.warn("corpId :" + corpid +", have no lose crm ids");
            return;
        }
        //日志 跟踪
        Map<Long/**usr_id**/, List<CrmDetailEntity>> map = CollectionUtil.generateUserIdWithCrmIds(crmDetailEntities);
        for (Map.Entry<Long, List<CrmDetailEntity>> entry : map.entrySet()) {
            long usrId = entry.getKey();
            List<CrmDetailEntity> crmids = entry.getValue();
            LOG.info("企业id为: ===>"+ corpid  + " 用户id 为 : ==>" + usrId +" 该用户下的掉单的客户数量为：" + crmids.size() +" 掉单客户详情为 ===》" + JSON.toJSONString(crmids));
        }

        LOG.info("search for the lose crm ,the lose corpId is :" + corpid +"====:lose crmIds size is ====>"+crmDetailEntities.size());
        //转换CrmDetailEntity ==> long  类型,该企业下的调单crmid
        List<Long> ids = CollectionUtil.convertListCrmDetail2Long(crmDetailEntities);

        //分页，更新数据库，调用es, 写nsq等业务逻辑
        PagingExcuteCrmId pagingExcuteCrmId = new PagingExcuteCrmId();
        pagingExcuteCrmId.setLoseCrmIds(crmDetailEntities);
        pagingExcuteCrmId.dealPageList(ids, configProperties.OPEAATION_NUMBER, (Long) corpid);
        // 分组发送通知，给IM
        UDPClientSocket udpClientSocket = new UDPClientSocket();   // 每次new？ 以后优化
        try {
            if (udpClientSocket != null) {
                //发系统通知提醒
                Map<Long/**usr_id**/, List<CrmDetailEntity>> userIdCrmMap = CollectionUtil.generateUserIdWithCrmIds(crmDetailEntities);
                for (Map.Entry<Long, List<CrmDetailEntity>> entry : userIdCrmMap.entrySet()) {
                    long usrId = entry.getKey();
                    final  String updataMessage = String.format("type=411+succnum=%d", 0);
                    LOG.info(String.format("通知 IM Server: userId=%1s, message=[%2s]", String.valueOf(usrId), updataMessage));
                    byte[] tipsDataPacket = Packet.packet(usrId, updataMessage);
                    udpClientSocket.send(configProperties.IM_HOST, configProperties.IM_PORT, tipsDataPacket);
                }
            }
        } catch (Exception e) {
                LOG.error("deal lose crm ,fail to send notice to the IM server ,the corpid is :" + corpid ,e);
        }finally {
            udpClientSocket.close();
        }

    }

    /**
     * 主要用于给定时线程提供的api
     * @param corpid
     * @param crmIds
     * @param stage 表示上一次调单执行到的阶段
     * @throws InterruptedException
     */
    public void dealFail2DeadLineCrmId(long corpid, List<Long> crmIds, int stage) throws InterruptedException {

        //查询该企业下的所有crmId
        List<CrmDetailEntity> crmDetailEntities = crmDetailDao.findCrmDetailsByCorpId(corpid);
        //定时扫描的时候在扫描一下已经掉单的crmid，
        List<CrmDetailEntity> loseCrmIds = searcherWillDeadLineDiaodanCrmIdByCorpId(0, corpid);

        List<Long> filterloseCrmds = CollectionUtil.filterHasNoLoseCrmId(loseCrmIds, crmIds);

        switch (stage) {
            case DiaodanConstants.UPDATE_DETAIL:
                //删除关系eccrm_detail
                dealupdateCrmDetailByCorpId(corpid, filterloseCrmds);
                //更新t_crm_relation
                dealupdateCrmRleationByCorpId( corpid, filterloseCrmds);
                //删除共享t_crm_share_relation
                dealdeleteCrmShareRelation(corpid, filterloseCrmds);
                //删除公司关系 t_crm_corp_relation
                deleteCrmCorpRelationCorpid( corpid, filterloseCrmds);
                //清除qq关系
                cleanQQrelactionShip( corpid, filterloseCrmds);
                //清楚销售计划
                cleanCrmPan( corpid, filterloseCrmds);
                //写入变更记录log表
                writeChangeLog(corpid, filterloseCrmds,crmDetailEntities);
                // 写ES
                dealCrm2ES( corpid, filterloseCrmds);
                //  写轨迹
                dealCrm2nsq(corpid, filterloseCrmds, crmDetailEntities);
                //更新上限值
                batchUpdateCrmLimit(corpid, filterloseCrmds,crmDetailEntities);
                //更新memecache
                batchUpdateMemcache( corpid, filterloseCrmds,crmDetailEntities);
                break;
            case  DiaodanConstants.UPDATE_CRM_RELATION:
                //更新t_crm_relation
                dealupdateCrmRleationByCorpId( corpid, filterloseCrmds);
                //删除共享t_crm_share_relation
                dealdeleteCrmShareRelation(corpid, filterloseCrmds);
                //删除公司关系 t_crm_corp_relation
                deleteCrmCorpRelationCorpid( corpid, filterloseCrmds);
                //清除qq关系
                cleanQQrelactionShip( corpid, filterloseCrmds);
                //清楚销售计划
                cleanCrmPan( corpid, filterloseCrmds);
                //写入变更记录log表
                writeChangeLog(corpid, filterloseCrmds,crmDetailEntities);
                // 写ES
                dealCrm2ES( corpid, filterloseCrmds);
                //  写轨迹
                dealCrm2nsq(corpid, filterloseCrmds, crmDetailEntities);
                //更新上限值
                batchUpdateCrmLimit(corpid, filterloseCrmds,crmDetailEntities);
                //更新memecache
                batchUpdateMemcache( corpid, filterloseCrmds,crmDetailEntities);
                break;
            case DiaodanConstants.DELETE_SHARE_RELATION:
                //删除共享t_crm_share_relation
                dealdeleteCrmShareRelation(corpid, filterloseCrmds);
                //删除公司关系 t_crm_corp_relation
                deleteCrmCorpRelationCorpid( corpid, filterloseCrmds);
                //清除qq关系
                cleanQQrelactionShip( corpid, filterloseCrmds);
                //清楚销售计划
                cleanCrmPan( corpid, filterloseCrmds);
                //写入变更记录log表
                writeChangeLog(corpid, filterloseCrmds,crmDetailEntities);
                // 写ES
                dealCrm2ES( corpid, filterloseCrmds);
                //  写轨迹
                dealCrm2nsq(corpid, filterloseCrmds, crmDetailEntities);
                //更新上限值
                batchUpdateCrmLimit(corpid, filterloseCrmds,crmDetailEntities);
                //更新memecache
                batchUpdateMemcache( corpid, filterloseCrmds,crmDetailEntities);
                break;
            case DiaodanConstants.DELETE_CRM_CORP_RELATION:
                //删除公司关系 t_crm_corp_relation
                deleteCrmCorpRelationCorpid( corpid, filterloseCrmds);
                //清除qq关系
                cleanQQrelactionShip( corpid, filterloseCrmds);
                //清楚销售计划
                cleanCrmPan( corpid, filterloseCrmds);
                //写入变更记录log表
                writeChangeLog(corpid, filterloseCrmds,crmDetailEntities);
                // 写ES
                dealCrm2ES( corpid, filterloseCrmds);
                //  写轨迹
                dealCrm2nsq(corpid, filterloseCrmds, crmDetailEntities);
                //更新上限值
                batchUpdateCrmLimit(corpid, filterloseCrmds,crmDetailEntities);
                //更新memecache
                batchUpdateMemcache( corpid, filterloseCrmds,crmDetailEntities);
                break;
            case DiaodanConstants.UPDATE_QQ:
                //清除qq关系
                cleanQQrelactionShip(corpid, filterloseCrmds);
                //清楚销售计划
                cleanCrmPan(corpid, filterloseCrmds);
                //写入变更记录log表
                writeChangeLog(corpid, filterloseCrmds,crmDetailEntities);
                // 写ES
                dealCrm2ES( corpid, filterloseCrmds);
                //  写轨迹
                dealCrm2nsq(corpid, filterloseCrmds, crmDetailEntities);
                //更新上限值
                batchUpdateCrmLimit(corpid, filterloseCrmds,crmDetailEntities);
                //更新memecache
                batchUpdateMemcache( corpid, filterloseCrmds,crmDetailEntities);
                break;
            case DiaodanConstants.UPDATE_CRM_PLAN:
                //清楚销售计划
                cleanCrmPan(corpid, filterloseCrmds);
                //写入变更记录log表
                writeChangeLog(corpid, filterloseCrmds,crmDetailEntities);
                // 写ES
                dealCrm2ES( corpid, filterloseCrmds);
                //  写轨迹
                dealCrm2nsq(corpid, filterloseCrmds, crmDetailEntities);
                //更新上限值
                batchUpdateCrmLimit(corpid, filterloseCrmds,crmDetailEntities);
                //更新memecache
                batchUpdateMemcache( corpid, filterloseCrmds,crmDetailEntities);
                break;

            case DiaodanConstants.UPDATE_CHANGE_LOG:
                //写入变更记录log表
                writeChangeLog(corpid, filterloseCrmds,crmDetailEntities);
                // 写ES
                dealCrm2ES( corpid, filterloseCrmds);
                //  写轨迹
                dealCrm2nsq(corpid, filterloseCrmds, crmDetailEntities);
                //更新上限值
                batchUpdateCrmLimit(corpid, filterloseCrmds,crmDetailEntities);
                //更新memecache
                batchUpdateMemcache( corpid, filterloseCrmds,crmDetailEntities);
                break;
            case DiaodanConstants.WRITE_ES:
                // 写ES
                dealCrm2ES( corpid, filterloseCrmds);
                //  写轨迹
                dealCrm2nsq(corpid, filterloseCrmds, crmDetailEntities);
                //更新上限值
                batchUpdateCrmLimit(corpid, filterloseCrmds,crmDetailEntities);
                //更新memecache
                batchUpdateMemcache( corpid, filterloseCrmds,crmDetailEntities);
                break;
            case DiaodanConstants.WRITE_NSQ:
                //  写轨迹
                dealCrm2nsq(corpid, filterloseCrmds, crmDetailEntities);
                //更新上限值
                batchUpdateCrmLimit(corpid, filterloseCrmds,crmDetailEntities);
                //更新memecache
                batchUpdateMemcache( corpid, filterloseCrmds,crmDetailEntities);
                break;
            case DiaodanConstants.UPDATE_REDIS_LIMIT:
                //更新上限值
                batchUpdateCrmLimit(corpid, filterloseCrmds,crmDetailEntities);
                //更新memecache
                batchUpdateMemcache( corpid, filterloseCrmds,crmDetailEntities);
                break;
            case DiaodanConstants.UPDATE_MEMCACHE:
                //更新memecache
                batchUpdateMemcache( corpid, filterloseCrmds,crmDetailEntities);
                break;
            default:
                throw new IllegalArgumentException("illegal stage .the stage is: " + stage);
        }
    }


    /**
     * 批量处理已经掉单的crmId
     * @param corpids
     * @param callBack
     */
    public void dealDeadLineDiaodanCrmIdsByCorpIds(List<CrmLoseRuleEntity> corpids,TaskCallBack callBack) {
        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setTaskPath(configProperties.DEAL_LOSE_PATH);
        try {
            for (CrmLoseRuleEntity crmLoseRuleEntity : corpids) {
                    dealDeadLineDiaodanCrmIdByCorpId(crmLoseRuleEntity.getF_crop_id());
            }
            taskEntity.setLastEndDate(new Date());
            taskEntity.setStarting(DiaodanConstants.EXECUT_SUCCESS);
        } catch (Exception e) {
            taskEntity.setStarting(DiaodanConstants.EXECUT_FAIL);
            taskEntity.setLastEndDate(new Date());
            LOG.error("fail to deal lose diao dan ",e);
        }finally {
            //回调，更新autoTask 表，更新任务状态
            callBack.notifyTaskTheResultOfcallService(taskEntity);
        }

    }

    /**
     * 处理已经掉单的crmid，暴露的服务给外面,
     *
     * 该服务会将所有企业ids下的diao
     *
     */
    public void dealDeadLineDiaodan() {

       final  List<CrmLoseRuleEntity> totalcCoprids = searcherSetDiaodanCorpIds();

        LOG.info("deal lose crm service has been call ,the scan of the total  corpId is : "+totalcCoprids.size());
        LOG.debug("scan of the corpids is  : " + JSON.toJSONString(totalcCoprids));

        if (client == null) {
            for(int i=0; i<3;i++){
                client = thriftClient.getThriftClient();  //初始化thrift,重试3次，有可能是网络问题
                if (client != null) {
                    break;
                }
                try {
                    Thread.sleep(2000);  // 休息2秒，重连
                } catch (InterruptedException e) {
                     //TODO
                }
            }
        }

        if (client == null) {
            LOG.error("fail to init thrift client , will fail to deal ES ");
            throw new RuntimeException("fail to connect the  ES,will not to deal diao dan service");
        }

        //线程，异步处理。拿到corpids,批量处理
        new Thread(new Runnable() {
            public void run() {
                dealDeadLineDiaodanCrmIdsByCorpIds(totalcCoprids, new TaskCallBack() {
                    public void notifyTaskTheResultOfcallService(TaskEntity entity) {
                        try {
                            taskDao.updateTask(entity);
                            LOG.info("...........deal lose CRM id is done..........");
                        } catch (Exception e) {
                            LOG.error("call back task ,fail to update task  ", e);
                        }finally{
                           thriftClient.close();
                        }
                    }
                });
            }
        },"dealLoseCrm").start();

    }

    /**
     * 处理即将掉单的
     */
    public void dealWillDeadLineDaoDan(TaskCallBack taskCallBack) {
        TaskEntity task = new TaskEntity();
        List<CrmDetailEntity> crmDetailEntities = null;
        UDPClientSocket udpClientSocket = null;
        task.setTaskPath(configProperties.DEAL_WILL_LOSE_PATH);
        try {
            List<CrmLoseRuleEntity> totalcCoprids = searcherSetDiaodanCorpIds();
            if (totalcCoprids == null || totalcCoprids.size() == 0) {
                LOG.info("finished deal will lose crm , have no coprid set lose rule");
                return;
            }
            List<LoseRecordEntity> loseRecordEntities = new ArrayList<LoseRecordEntity>();
            udpClientSocket = new UDPClientSocket();
            //封装loseRecordEntity数据
            for (CrmLoseRuleEntity crmLoseRuleEntity : totalcCoprids) {
                long corpid = crmLoseRuleEntity.getF_crop_id();
                crmDetailEntities = searcherWillDeadLineDiaodanCrmIdByCorpId(48, corpid); // 即将调单的
                LOG.info("will lose crimid ,corpId : " + corpid +" lose crimdis json :" + JSON.toJSONString(crmDetailEntities));
                if (crmDetailEntities == null) {
                    continue;
                }
                for (CrmDetailEntity crmDetailEntity : crmDetailEntities) {
                    LoseRecordEntity loseRecordEntity = new LoseRecordEntity();
                    loseRecordEntity.setF_corp_id(corpid);
                    loseRecordEntity.setF_crm_id(crmDetailEntity.getF_crm_id());
                    loseRecordEntity.setF_lose_time(crmDetailEntity.getF_lose_time());
                    loseRecordEntity.setF_last_contact_name(crmDetailEntity.getF_new_contact_time()==""?null:crmDetailEntity.getF_new_contact_time());
                    loseRecordEntity.setF_usr_id(crmDetailEntity.getF_user_id());
                    loseRecordEntity.setF_type(crmDetailEntity.getF_type());
                    loseRecordEntity.setF_createTime(DateUtil.convertDate2String(new Date()));
                    loseRecordEntities.add(loseRecordEntity);
                }
                // 入库
                diaodanDao.updateLoseRecord(loseRecordEntities, corpid); //批量replace 即将掉单表
                task.setStarting(DiaodanConstants.EXECUT_SUCCESS);
                task.setLastEndDate(new Date());
                LOG.info("will lose dan service  success  finished  db operation , the corpId is: " + corpid);

                //send tips
                //udpClientSocket.setSoTimeout();
                if (udpClientSocket != null) {
                    //发系统通知提醒
                    Map<Long/**usr_id**/, List<CrmDetailEntity>> userIdCrmMap = CollectionUtil.generateUserIdWithCrmIds(crmDetailEntities);
                    if(userIdCrmMap!=null) {
                        for (Map.Entry<Long, List<CrmDetailEntity>> entry : userIdCrmMap.entrySet()) {
                            long usrId = entry.getKey();
                            List<CrmDetailEntity> crmDetailEntities1 = entry.getValue();
                            int size = crmDetailEntities1.size();
                            final String tipsMessage = String.format(DiaodanConstants.LOSE_REMIND_FORMAT, size);
                            LOG.info("send lose tips ,the tipsMessage is :" + tipsMessage + "======userId is ====" + usrId);
                            byte[] tipsDataPacket = Packet.packet(usrId, tipsMessage);
                            udpClientSocket.send(configProperties.IM_HOST, configProperties.IM_PORT, tipsDataPacket);
                        }
                    }
                    LOG.info("send lose remind tips success");
                }
            }
        } catch (Exception e) {
            LOG.error("fail to deal with will lose dan",e);
            task.setStarting(DiaodanConstants.EXECUT_FAIL);  //执行失败
            task.setLastEndDate(new Date());
        }finally {
            taskCallBack.notifyTaskTheResultOfcallService(task);
            if(udpClientSocket != null)
               udpClientSocket.close();
        }
    }

    /**
     *  暴露的dubbo服务，在48小时内，提醒即将调单的
     */
     public void   dealWillLoseDan(){

           LOG.info("begin deal will lose dan service ,the time is :" + new Date().toLocaleString());
            new Thread(new Runnable() {
                public void run() {
                    dealWillDeadLineDaoDan(new TaskCallBack() {
                        public void notifyTaskTheResultOfcallService(TaskEntity entity) {
                            taskDao.updateTask(entity);
                            LOG.info("...........scan will lose crm is done..........");
                        }
                    });
                }
            }).start();
     }



    /**
     * 查询出crmid，在detail表里，而不在contactTime表里的crmid
     * @param corpId
     * @return
     */
    public List<CrmDetailEntity> findNotIncontactTimeTableCrmId(long corpId,String  effectiveType) {
        //获得在cmrdetail下的crmid，这里面表示的是corpId下的整个cmrdetail数据
        List<CrmDetailEntity> crmDetailEntities = crmDetailDao.findCrmDetailsByCorpId(corpId);
        if (crmDetailEntities == null) {
            return null;
        }
        //获得contactTime表里的crmid，需要去重
        List<CrmContactTimeEntity> crmContactTimeEntities = crmContactTimeDao.findCrmOpearationTypeInEffecitiveCallWays(corpId, effectiveType);
        if (crmContactTimeEntities == null) {
            crmContactTimeEntities = new ArrayList<CrmContactTimeEntity>();
        }
        // 取差
        List<CrmDetailEntity> copyOfCrm = CollectionUtil.inCrmDetailNotIncontactTime(crmDetailEntities,crmContactTimeEntities);
        return copyOfCrm;

    }


    private void remvoeDuplicateCrmIdAndfilterNotContainTag(List<CrmDetailEntity> crmDetailEntities,String[] tags) {
        List<CrmDetailEntity> needFilter = new ArrayList<CrmDetailEntity>();
        for (CrmDetailEntity crmDetailEntity : crmDetailEntities) {
            long crmid = crmDetailEntity.getF_crm_id();
            long corpId = crmDetailEntity.getF_corp_id();
            List<CrmDetailClassEntity> crmDetailClassEntities = crmDetailClassDao.findCrmdetailClasssByCrimId(crmid, corpId);
            if (crmDetailClassEntities.size() == 0) {
                needFilter.add(crmDetailEntity);
            }
            if (crmDetailClassEntities.size() > 0) {
                boolean isContainTag = false;
                for (int i = 0; crmDetailClassEntities != null && i < crmDetailClassEntities.size(); i++) { //2494808434,2494808802
                    long crmclassId = crmDetailClassEntities.get(i).getF_class_id();
                    String classId = crmclassId + "";
                    if (StringUtil.stringIsContainStringArrays(tags, classId)) {
                        isContainTag = true;
                        break;
                    }
                }
                if (!isContainTag) {
                    needFilter.add(crmDetailEntity);
                }
            }
        }
        crmDetailEntities.removeAll(needFilter);
        LOG.info("according to tags filter crmId, the left crms size :" + crmDetailEntities.size());
    }

    public class PagingExcuteCrmId extends AbstractPagingHandler<Long> {

        private final Logger crmLog = LogManager.getLogger(PagingExcuteCrmId.class);
        private List<CrmDetailEntity> loseCrmIds = null;  //整个企业id下掉单的crmId

        public void setLoseCrmIds(List<CrmDetailEntity> loseCrmIds) {
            this.loseCrmIds = loseCrmIds;
        }

        @Override
        protected void beforePagingDealListCrmid() {
             //todo
        }

        @Override
        protected void afterPagingDealListCrmid() {
          //todo
        }

        @Override
        protected void dealExceptionWhenPageingDealL(Throwable throwable) {
                 //TODO
        }

        public void pagingDealListCrmid(List collections, Object corpId) throws InterruptedException {

            //删除关系eccrm_detail
            dealupdateCrmDetailByCorpId((Long) corpId, collections);
            //更新t_crm_relation
            dealupdateCrmRleationByCorpId((Long) corpId, collections);
            //删除共享t_crm_share_relation
            dealdeleteCrmShareRelation((Long) corpId, collections);
            //删除公司关系 t_crm_corp_relation
            deleteCrmCorpRelationCorpid((Long) corpId, collections);
             //清除qq关系
            cleanQQrelactionShip((Long) corpId, collections);
            //清楚销售计划
            cleanCrmPan((Long) corpId, collections);
            //写入变更记录log表
            writeChangeLog((Long) corpId, collections,loseCrmIds);
           // 写ES
            dealCrm2ES((Long) corpId, collections);
           //  写轨迹
            dealCrm2nsq((Long) corpId, collections, loseCrmIds);
            //更新上限值
            batchUpdateCrmLimit((Long) corpId, collections,loseCrmIds);
            //更新memecache
            batchUpdateMemcache((Long) corpId, collections,loseCrmIds);
        }
    }

    //清除关系crm_detail 表
    private void dealupdateCrmDetailByCorpId(long corpID, List<Long> crmIds) throws  InterruptedException{
        try {
            LOG.info("....................begin to update crm detail........... [coprid] " + corpID);
            crmDetailDao.updateCrmDetailByCorpId(corpID, crmIds);       // collections,为每次更新的调单crmid 集合
        } catch (Exception e) {
            LOG.error("fail to update crm detail ,the corpId is" + corpID + ",the crmIds size is :" + crmIds.size(), e);
            LoseCrmId crmId = new LoseCrmId();
            crmId.setCrmIds(crmIds);
            crmId.setCorpId(corpID);
            crmId.setStage(0);
            SingleOutContext.FAIL_TO_EXCUTE_LOSECRM.put(crmId);        //写入阻塞队列
            throw new RuntimeException("fail to updateCrmDetail By CorpId",e);
        }
    }

    //更新共享关系，t_crm_relation
    private void dealupdateCrmRleationByCorpId(long corpID, List<Long> crmIds)throws  InterruptedException{
        try {
            LOG.info("....................begin to update crm relation ........... [coprid] " + corpID);
            crmRelationDao.updateCrmRleationByCorpId(corpID, crmIds);
        } catch (Exception e) {
            LOG.error("fail to update crm relationShip ,the corpId is"+ corpID +",the crmIds size is :" + crmIds.size(),e);
            LoseCrmId crmId = new LoseCrmId();
            crmId.setCrmIds(crmIds);
            crmId.setCorpId(corpID);
            crmId.setStage(1);
            SingleOutContext.FAIL_TO_EXCUTE_LOSECRM.put(crmId);        //写入阻塞队列
            throw new RuntimeException("fail to updateCrmRleationByCorpId By CorpId",e);
        }
    }

    //删除共享关系t_crm_share_relation
    private void dealdeleteCrmShareRelation(long corpID, List<Long> crmIds)throws  InterruptedException{
        try {
            LOG.info("....................begin to delete crm share relation........... [coprid] " + corpID);
            crmShareRelationDao.deleteCrmShareRelation(corpID, crmIds);
        } catch (Exception e) {
            LOG.error("fail to delete crm shareRelationShip ,the corpId is"+ corpID +",the crmIds size is :" + crmIds.size(),e);
            LoseCrmId crmId = new LoseCrmId();
            crmId.setCrmIds(crmIds);
            crmId.setCorpId(corpID);
            crmId.setStage(2);
            SingleOutContext.FAIL_TO_EXCUTE_LOSECRM.put(crmId);        //写入阻塞队列
            throw new RuntimeException("fail to deleteCrmShareRelation By CorpId",e);
        }
    }

    //删除公司关系t_crm_corp_relation
    private void deleteCrmCorpRelationCorpid(long corpID, List<Long> crmIds)throws  InterruptedException{
        try {
            LOG.info("....................begin to delete crm_corp_relation........... [coprid] " + corpID);
            crmCorpRelationDao.deleteCrmCoprRelationShip(corpID, crmIds);
        } catch (Exception e) {
            LOG.error("fail to update crmCorp  ,the corpId is"+ corpID +",the crmIds size is :" + crmIds.size(),e);
            LoseCrmId crmId = new LoseCrmId();
            crmId.setCrmIds(crmIds);
            crmId.setCorpId(corpID);
            crmId.setStage(3);
            SingleOutContext.FAIL_TO_EXCUTE_LOSECRM.put(crmId);        //写入阻塞队列
            throw new RuntimeException("fail to updateCrmCorpByCorpid By CorpId",e);

        }
    }

    //清除qq关系
    private void cleanQQrelactionShip(long corpId,List<Long> crmids) throws  InterruptedException{
        try {
            LOG.info("....................begin to clear qq relationship.......... [coprid] " + corpId);
            boolean isNewCorp = crmDetailDao.isNewCorp(corpId);
            if (isNewCorp) {
                LOG.info("corpId is new corp, not need update QQ,the corpId is :"+corpId);
                return;
            }
            qqClassInfoDao.updateDecQQclassInfoByCrmIds(crmids);
        } catch (Exception e) {
            LOG.error("fail to update QQ  ,the corpId is"+ corpId +",the crmIds size is :" + crmids.size(),e);
            LoseCrmId crmId = new LoseCrmId();
            crmId.setCrmIds(crmids);
            crmId.setCorpId(corpId);
            crmId.setStage(DiaodanConstants.UPDATE_QQ);
            SingleOutContext.FAIL_TO_EXCUTE_LOSECRM.put(crmId);        //写入阻塞队列
            throw new RuntimeException("fail to updateCrmCorpByCorpid By CorpId",e);

        }
    }


    //批量清除销售计划
    private void cleanCrmPan(long corpId, List<Long> crmIds) throws  InterruptedException{
        try {
            LOG.info("....................begin to clear crm  plan ........... [coprid] " + corpId);
            boolean isNewCorp = crmDetailDao.isNewCorp(corpId);
            if (isNewCorp) {
                LOG.info("corpId is new corp, not need update QQ,the corpId is :"+corpId);
                return;
            }
            crmPlanDetailDao.updateCrmPlan(corpId, crmIds);
            crmPlanDetailDao.deleteCrmPlan(corpId, crmIds);
        } catch (Exception e) {
            LOG.error("fail to update CrmPan  ,the corpId is"+ corpId +",the crmIds size is :" + crmIds.size(),e);
            LoseCrmId crmId = new LoseCrmId();
            crmId.setCrmIds(crmIds);
            crmId.setCorpId(corpId);
            crmId.setStage(DiaodanConstants.UPDATE_QQ);
            SingleOutContext.FAIL_TO_EXCUTE_LOSECRM.put(crmId);        //写入阻塞队列
            throw new RuntimeException("fail to updateCrmCorpByCorpid By CorpId",e);
        }
    }

    /**
     *
     * @param corpId
     * @param crmIds
     * @param crmDetailEntities  执行业务操作前，所有掉单的crmId
     */
    private void writeChangeLog(long corpId, List<Long> crmIds,List<CrmDetailEntity> crmDetailEntities) throws  InterruptedException{
        try {
            LOG.info("....................begin to write Change log........... [coprid] " + corpId);
            List<CrmchangeLogEntity> changeLogs = CollectionUtil.convertLong2CrmChangeLog(crmIds, crmDetailEntities);
            crmChangeLogDao.updateCrmChangeLog(corpId, changeLogs);
            crmChangeOnceDao.updateCrmChangeOnce(corpId, changeLogs);
            crmChangeDao.updateCrmChange(corpId, changeLogs);
        } catch (Exception e) {
            LOG.error("fail to wite changeLog  ,the corpId is"+ corpId +",the crmIds size is :" + crmIds.size(),e);
            LoseCrmId crmId = new LoseCrmId();
            crmId.setCrmIds(crmIds);
            crmId.setCorpId(corpId);
            crmId.setStage(DiaodanConstants.UPDATE_CHANGE_LOG);
            SingleOutContext.FAIL_TO_EXCUTE_LOSECRM.put(crmId);        //写入阻塞队列
            throw new RuntimeException("fail to updateCrmCorpByCorpid By CorpId",e);
        }
    }


    //批量更新上限
    private void batchUpdateCrmLimit(long corpid, List<Long> crmIds,List<CrmDetailEntity> crmDetailEntities) throws InterruptedException{
        try {
            LOG.info("....................begin to update redis crmLimit............ [coprid] " + corpid);
            List<CrmDetailEntity> loseDetail = CollectionUtil.convertLong2CrmDetail(crmIds, crmDetailEntities);
            if (loseDetail == null || loseDetail.size() == 0) {
                return;
            }
            Map<Long/**usr_id**/, List<CrmDetailEntity>> usrMap = CollectionUtil.generateUserIdWithCrmIds(loseDetail);
            for (Map.Entry<Long, List<CrmDetailEntity>> entry : usrMap.entrySet()) {
                long usr_id = entry.getKey();
                List<CrmDetailEntity> value = entry.getValue();
                // 清除mc客户、公司分组统计缓存
                String limitKey = DiaodanConstants.CRM_RDIS_KEY + usr_id;
                int size = value.size();
                redisTemplate.set(limitKey, redisTemplate.get(limitKey) + size);
            }
        } catch (Exception e) {
            LOG.error("fail to  update redis crm  limit  ,the corpId is" + corpid + ",the crmIds size is :" + crmIds.size(), e);
            LoseCrmId crmId = new LoseCrmId();
            crmId.setCrmIds(crmIds);
            crmId.setCorpId(corpid);
            crmId.setStage(DiaodanConstants.UPDATE_REDIS_LIMIT);
            SingleOutContext.FAIL_TO_EXCUTE_LOSECRM.put(crmId);        //写入阻塞队列
            throw new RuntimeException("fail to write ES  By CorpId",e);
        }

    }

    // 更新memecache
    private void batchUpdateMemcache(long corpId, List<Long> crmIds,List<CrmDetailEntity> crmDetailEntities) throws InterruptedException{
        try {
            LOG.info("....................begin to update memcache.............. [coprid] " + corpId);
            List<CrmDetailEntity> loseDetail = CollectionUtil.convertLong2CrmDetail(crmIds, crmDetailEntities);
            Map<Long/**usr_id**/, List<CrmDetailEntity>> usrMap = CollectionUtil.generateUserIdWithCrmIds(loseDetail);
            if(usrMap!=null) {
                for (Map.Entry<Long, List<CrmDetailEntity>> entry : usrMap.entrySet()) {
                    long usr_id = entry.getKey();
                    // 清除mc客户、公司分组统计缓存
                    String corpGroupKey = DiaodanConstants.CORP_GROUP_NUM + usr_id;
                    String crmGroupKey = DiaodanConstants.CRM_GROUP_NUM + usr_id;
                    memcachedDao.deleteByKey(corpGroupKey);
                    memcachedDao.deleteByKey(crmGroupKey);
                }
            }
        } catch (Exception e) {
            LOG.error("fail to  update memcache  ,the corpId is" + corpId + ",the crmIds size is :" + crmIds.size(), e);
            LoseCrmId crmId = new LoseCrmId();
            crmId.setCrmIds(crmIds);
            crmId.setCorpId(corpId);
            crmId.setStage(DiaodanConstants.UPDATE_MEMCACHE);
            SingleOutContext.FAIL_TO_EXCUTE_LOSECRM.put(crmId);        //写入阻塞队列
            throw new RuntimeException("fail to write ES  By CorpId",e);
        }

    }

   //　批量更新ES
    private void dealCrm2ES(long corpID, List<Long> crmIds) throws InterruptedException{

        try {
            if (client == null) {
                if (client == null) {
                    for(int i=0; i<3;i++){
                        client = thriftClient.getThriftClient();  //初始化thrift,重试3次，有可能是网络问题
                        if (client != null) {
                            break;
                        }
                        try {
                            Thread.sleep(2000);  // 休息2秒，重连
                        } catch (InterruptedException e) {
                            //TODO
                        }
                    }
                }
                if (client == null) {
                    LOG.error("fail to init thrift client , will fail to deal ES ");
                    throw new RuntimeException("fail to connect the  ES,will not to deal diao dan service");
                }
            }
            LOG.info("....................begin write crm 2 ES............. [coprid] " + corpID);
            client.batchInsertOrUpdate(corpID, crmIds);
        } catch (TException e) {
            LOG.error("fail to  write crmId to ES  ,the corpId is :====>" + corpID + ",the crmIds size is :" + crmIds.size(), e);
            LoseCrmId crmId = new LoseCrmId();
            crmId.setCrmIds(crmIds);
            crmId.setCorpId(corpID);
            crmId.setStage(DiaodanConstants.WRITE_ES);
            SingleOutContext.FAIL_TO_EXCUTE_LOSECRM.put(crmId);        //写入阻塞队列
            throw new RuntimeException("fail to write ES  By CorpId",e);
        }
    }
    // 写轨迹到nsq

    private void dealCrm2nsq(long corpID, List<Long> crmIds,List<CrmDetailEntity> crmDetailEntities) throws InterruptedException {
        try {
            LOG.info("....................begin write trial crm to nsq.............. [coprid] " + corpID);
            List<CrmDetailEntity> loseDetail = CollectionUtil.convertLong2CrmDetail(crmIds, crmDetailEntities);
            List<WriteNsqJson> writeNsqJsons = JsonUtil.chanageCrmDetail2WriteNsqJsons(loseDetail);
            nsqHandler.writeCustomerTrail(writeNsqJsons, corpID);
        } catch (Exception e) {
            LOG.error("fail to write the trail to Nsq  ,the corpId is"+ corpID +",the crmIds size is :" + crmIds.size(),e);
            LoseCrmId crmId = new LoseCrmId();
            crmId.setCrmIds(crmIds);
            crmId.setCorpId(corpID);
            crmId.setStage(DiaodanConstants.WRITE_NSQ);
            SingleOutContext.FAIL_TO_EXCUTE_LOSECRM.put(crmId);        //写入阻塞队列
            throw new RuntimeException("fail to write the trail to nsq  By CorpId",e);
        }
    }

}



