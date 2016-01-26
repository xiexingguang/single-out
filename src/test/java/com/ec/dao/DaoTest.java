package com.ec.dao;

import com.alibaba.fastjson.JSON;
import com.ec.singleOut.dao.*;
import com.ec.singleOut.entity.*;
import com.ec.singleOut.json.Data;
import com.ec.singleOut.json.DefaultMessage;
import com.ec.singleOut.json.WriteNsqJson;
import com.ec.singleOut.nsq.ECNsqProducer;
import com.ec.singleOut.nsq.NsqHandler;
import com.ec.singleOut.redis.RedisTemplate;
import com.ec.singleOut.thrift.api.EsProxyService;
import com.ec.singleOut.thrift.client.ThriftClient;
import com.ec.singleOut.util.DateUtil;
import com.trendrr.nsq.NSQConsumer;
import com.trendrr.nsq.NSQLookup;
import com.trendrr.nsq.NSQMessage;
import com.trendrr.nsq.NSQMessageCallback;
import com.trendrr.nsq.lookup.NSQLookupDynMapImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by ecuser on 2016/1/5.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
        locations = {"classpath:spring/applicationContext.xml"}
)
public class DaoTest{

    @Autowired
    private CrmContactTimeDao crmContactTimeDao;
    @Autowired
    private CrmLoseRuleDao crmLoseRuleDao;
    @Autowired
    private CrmDetailDao crmDetailDao;
    @Autowired
    private CrmRelationDao crmRelationDao;
   @Autowired
    private CrmShareRelationDao crmShareRelationDao;
   @Autowired
    private CrmCorpRelationDao crmCorpRelationDao;
     @Autowired
    private CrmPlanDetailDao crmPlanDetailDao;
    @Autowired
    private CrmChangeDao crmChangeDao;
    @Autowired
    private CrmChangeOnceDao crmChangeOnceDao;
    @Autowired
    private CrmChangeLogDao crmChangeLogDao;

    @Autowired
    private TaskDao taskDao;
    @Autowired
    private CrmDetailClassDao crmDetailClassDao;

    @Autowired
    private ThriftClient thriftClient;

    @Autowired
    private NsqHandler nsqHandler;

    @Autowired
    private ECNsqProducer ecNsqProducer;

    @Autowired
    private MemcachedDao memcachedDao;

    @Autowired
    private RedisTemplate redisTemplate;

   @Autowired
    private DiaodanDao diaodanDao;

    @Test
    public void testredisTemplate() {
        System.out.println(redisTemplate);
    }

     @Test
    public void testConnection() {
         System.out.println("crm0=====>:"+crmContactTimeDao.getSqlSessionCrm0());
         System.out.println("crm1=====>:"+crmContactTimeDao.getSqlSessionCrm1());
         System.out.println("crm2=====>:"+crmContactTimeDao.getSqlSessionCrm2());
         System.out.println("crm3=====>:"+crmContactTimeDao.getSqlSessionCrm3());
         System.out.println("static=====>:"+crmContactTimeDao.getSqlSessionStatic());

    }

    @Test
    public void testCrmContactTimeDao() {
        List<CrmContactTimeEntity> crmContactTimeEntities = crmContactTimeDao.findCrmOpearationTypeInEffecitiveCallWays(65292, "2,6");
        System.out.println(crmContactTimeEntities.size());
    }

    @Test
    public void testLoseRuleDao() {
       /* CrmLoseRuleEntity crmLoseRuleEntity = crmLoseRuleDao.searcherRulesByCorpId(62800);
        System.out.println(JSON.toJSON(crmLoseRuleEntity));*/

        List<CrmLoseRuleEntity> crmLoseRuleEntities = crmLoseRuleDao.searcherRulesFromCrm0();
        List<CrmLoseRuleEntity> crmLoseRuleEntities1 = crmLoseRuleDao.searcherRulesFromCrm1();
        System.out.println("crm0====>"+crmLoseRuleEntities.size());
        System.out.println("crm1====>"+crmLoseRuleEntities1.size());

    }

    @Test
    public void testCrmDetailDao() {

        // 出现问题，就是多了一个type字段为0的值
       /* List<CrmDetailEntity> crmDetailEntities = crmDetailDao.findCrmDetailsByCorpId(57591);
        System.out.println(crmDetailEntities.size());
        System.out.println("crmdetailEntities:"+JSON.toJSON(crmDetailEntities));

        CrmDetailEntity crmDetailEntity = crmDetailDao.findCrmDetailByCorpIdAndCrmId(57591, 110020677);
        System.out.println("CRM=============>"+JSON.toJSONString(crmDetailEntity));*/

        boolean flag = crmDetailDao.isNewCorp(59272);
        System.out.println(flag);

        List<Long> crmids = new ArrayList<Long>();
        crmids.add(110026775L);
      // crmids.add(110020677L);
        crmDetailDao.updateCrmDetailByCorpId(59272, crmids);

    }


    @Test
    public void testCrmRelation() {
        List<Long> crmids = new ArrayList<Long>();
        List<CrmDetailEntity> crmDetailEntities = new ArrayList<>();
        crmDetailEntities.add(new CrmDetailEntity().setF_crm_id(110020671));
        crmDetailEntities.add(new CrmDetailEntity().setF_crm_id(110020684));

        crmids.add(110026775L);
       // crmids.add(110020677L);
   //  crmRelationDao.updateCrmRleationByCorpId(59272, crmids);
      List<Long> cri =   crmRelationDao.searcherCrmIdHasNoSetTag(21299, crmDetailEntities);
        System.out.println("++++++++++++++++++" + JSON.toJSON(cri));
    }


    @Test
    public void testCrmShareRelation() {

        List<Long> crmids = new ArrayList<Long>();
        crmids.add(110178614L);
        crmids.add(110178744L);
        crmids.add(110178664L);
        crmShareRelationDao.deleteCrmShareRelation(63440, crmids);
    }

    @Test
    public void testCrmCorpRelationDao() {

    }

    @Test
    public void testCrmPlanDetailDao() {
        List<Long> crmids = new ArrayList<Long>();
        crmids.add(110022302L);
        crmids.add(110023218L);
        //crmPlanDetailDao.updateCrmPlan(59272, crmids);
        crmPlanDetailDao.deleteCrmPlan(59272,crmids);
    }

    @Test
    public void testChangeLogDao() {
        List<CrmchangeLogEntity> crmchangeLogEntities = new ArrayList<CrmchangeLogEntity>();
        CrmchangeLogEntity crmchangeLogEntity = new CrmchangeLogEntity();
        crmchangeLogEntity.setF_crm_id(999999L);
        crmchangeLogEntity.setF_memo("dian delect");
        crmchangeLogEntity.setF_corp_id(5003740);
        crmchangeLogEntity.setF_user_id(1117777);
        crmchangeLogEntity.setF_time(DateUtil.convetDate2Int(new Date()) + "");
        Date date = new Date();
        long tt = date.getTime();
        System.out.println("before===="+tt);
        String ttk = tt+"";
        System.out.println("kkkkk"+ttk);
        System.out.println(ttk.substring(ttk.length() - 3, ttk.length()));
        int intdate = Integer.parseInt(ttk.substring(0,ttk.length() - 3));
        System.out.println("after" + intdate);
        CrmchangeLogEntity crmchangeLogEntity1 = new CrmchangeLogEntity();
        crmchangeLogEntity1.setF_crm_id(999995L);
        crmchangeLogEntity1.setF_corp_id(5003740);
        crmchangeLogEntity1.setF_memo("dian delect");
        crmchangeLogEntity1.setF_user_id(1117777);
        crmchangeLogEntity1.setF_time(DateUtil.convetDate2Int(new Date())+"");
        CrmchangeLogEntity crmchangeLogEntity2= new CrmchangeLogEntity();
        crmchangeLogEntity2.setF_crm_id(999993L);
        crmchangeLogEntity2.setF_user_id(1117777);
        crmchangeLogEntity2.setF_corp_id(5003740);
        crmchangeLogEntity2.setF_memo("dian delect");
        crmchangeLogEntity2.setF_time(DateUtil.convetDate2Int(new Date()) + "");

        crmchangeLogEntities.add(crmchangeLogEntity);
        crmchangeLogEntities.add(crmchangeLogEntity1);
        crmchangeLogEntities.add(crmchangeLogEntity2);

        //crmChangeLogDao.updateCrmChangeLog(5003740, crmchangeLogEntities);
     //   crmChangeOnce.updateCrmChangeOnce(5003740,crmchangeLogEntities);
       crmChangeDao.updateCrmChange(5003740,crmchangeLogEntities);

    }

    public void testChangDao() {

    }

    public void testChangeOnceDao() {


    }

    @Test
    public void testDetailClass() {
        List<CrmDetailClassEntity> crmDetailClassEntities = crmDetailClassDao.findCrmdetailClasssByCrimId(160681873,59340);
        System.out.println(JSON.toJSON(crmDetailClassEntities));

    }

    @Test
    public void testTask() {
        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setTaskPath("dealLoseCRM");
        taskEntity.setLastEndDate(new Date());
        taskEntity.setStarting("2");
        taskDao.updateTask(taskEntity);

    }

    @Test
    public void testEs()throws  Exception{
        long corpid = 538984;
        List<Long> crmids = new ArrayList<Long>();
        crmids.add(110178614L);
        crmids.add(110178744L);
        crmids.add(110178664L);
        EsProxyService.Client client = thriftClient.getThriftClient();
        client.batchInsertOrUpdate(corpid, crmids);

    }

    @Test
    public void testNsq() {
      nsqHandler.writeCustomerTrail(null, 0L);
    }

    @Test
    public void testEcProducer() {
        System.out.println(ecNsqProducer.getBatchProducer("crmcontact", 1));
    }

    @Test
    public void testNsqJson()throws  Exception{
        Data data = new Data();
        DefaultMessage defaultMessage = new DefaultMessage();
        WriteNsqJson writeNsqJson1 = new WriteNsqJson();

        defaultMessage.setF_contact_num("");
        defaultMessage.setF_content("掉单eeeeeeeeeeeeeeeeeeeee");
        defaultMessage.setF_creat_time(DateUtil.convertDate2String(new Date()));
        defaultMessage.setF_crm_id(131313);
        defaultMessage.setF_from(10);
        defaultMessage.setF_is_addclient(0);
        defaultMessage.setF_msg_type(0);
        defaultMessage.setF_operate_type(116);
        defaultMessage.setF_ontime_flag(0);
        defaultMessage.setF_provice("");
        defaultMessage.setF_static_time(0);
        defaultMessage.setF_style(3);
        defaultMessage.setF_type(1);
        defaultMessage.setF_user_id(123123);
        defaultMessage.setF_waste_id("");
        defaultMessage.setF_corp_id(5009923);

        data.setDefault_message(defaultMessage);
        data.setGroup(1);
        data.setSubtype("");

        writeNsqJson1.setData(data);
        writeNsqJson1.setId("0");
        writeNsqJson1.setType("1");
        String jsonss = JSON.toJSONString(writeNsqJson1);
        System.out.println(JSON.toJSON(writeNsqJson1));
        ecNsqProducer.getBatchProducer("crmcontact", 1).produce("crmcontact",jsonss.getBytes());
    }


    @Test
    public void testMeched() {
        System.out.println(memcachedDao);
    }


    @Test
    public void testMonitor() {
        List<LoseRecordEntity> loseRecordEntities = new ArrayList<LoseRecordEntity>();
        LoseRecordEntity loseRecordEntity = new LoseRecordEntity();
        loseRecordEntity.setF_corp_id(13);
     //   loseRecordEntity.setF_createTime(new Date().toString());
        loseRecordEntity.setF_crm_id(3131);
        loseRecordEntity.setF_type(2);
        loseRecordEntity.setF_usr_id(99);

        LoseRecordEntity loseRecordEntity1 = new LoseRecordEntity();
        loseRecordEntity1.setF_corp_id(133333);
        loseRecordEntity1.setF_createTime(DateUtil.convertDate2String(new Date()));
        loseRecordEntity1.setF_crm_id(5555);
        loseRecordEntity1.setF_type(34);
        loseRecordEntity1.setF_usr_id(9911);

       loseRecordEntities.add(loseRecordEntity);
     loseRecordEntities.add(loseRecordEntity1);

  // diaodanDao.updateLoseRecord(loseRecordEntities, 59340);

        diaodanDao.truncateCrm1LoseReocrd();

    }







    @Test
    public void testConsumer() {

        NSQLookup lookup = new NSQLookupDynMapImpl();
        lookup.addAddr("10.0.200.51", 1175);
        NSQConsumer consumer = new NSQConsumer(lookup, "crmcontact", "java", new NSQMessageCallback() {

            @Override
            public void message(NSQMessage message) {
                System.out.println("received: " + message);
                //now mark the message as finished.
                message.finished();

                //or you could requeue it, which indicates a failure and puts it back on the queue.
                //message.requeue();
            }
            @Override
            public void error(Exception x) {
                //handle errors
              //  log.warn("Caught", x);
            }
        });

        consumer.start();



    }

}
