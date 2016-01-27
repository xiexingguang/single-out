package com.ec.dao;

import com.ec.singleOut.entity.CrmDetailEntity;
import com.ec.singleOut.service.DiaodanService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Created by ecuser on 2016/1/7.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
        locations = {"classpath:spring/applicationContext.xml"}
)
public class ServiceTest {

    @Autowired
    private DiaodanService diaodanService;

    @Test
    public void testService() {
        System.out.println(diaodanService.searcherSetDiaodanCorpIds().size());
    }

    @Test
    public void testSearchLoseCrMiD() {
        //5003740
   //     System.out.println("===============>size =======>"+diaodanService.searcherWillDeadLineDiaodanCrmIdByCorpId(0, 5009923).size());
       // System.out.println(JSON.toJSONString(diaodanService.searcherWillDeadLineDiaodanCrmIdByCorpId(0, 5003740),true));
        List<CrmDetailEntity> crmDetailEntities = diaodanService.searcherWillDeadLineDiaodanCrmIdByCorpId(0, 59340);
        System.out.println("size ====>" +crmDetailEntities.size());
        for (CrmDetailEntity crmDetailEntity : crmDetailEntities) {
            System.out.print(crmDetailEntity.getF_crm_id() + ",");
            if (crmDetailEntity.getF_user_id() == 5009933) {
                System.out.println("usr id  500933  下掉单的用户crmid ：" + crmDetailEntity.getF_crm_id());
            }
        }
   //      System.out.println("diao dan bean ====>" + JSON.toJSONString(diaodanService.searcherWillDeadLineDiaodanCrmIdByCorpId(0, 5009923)));
    }

    @Test
    public void testSearWillLoseCrmId() {
        List<CrmDetailEntity> crmDetailEntities = diaodanService.searcherWillDeadLineDiaodanCrmIdByCorpId(0, 59340);
        System.out.println("will crmid  ================ >>>" +  crmDetailEntities.size());
       // System.out.println("will lose crm id " + JSON.toJSONString(crmDetailEntities, true));
    }


    @Test
    public void testDealLoseCrm() {
        diaodanService.dealDeadLineDiaodanCrmIdByCorpId(59340);
    }

    @Test
    public void testDealLoseJob() throws  Exception{
        diaodanService.dealDeadLineDiaodan();
        Thread.sleep(2000000000L);
    }

    @Test
    public void testWillDealLoseJob() throws  Exception {
        diaodanService.dealWillLoseDan();
        Thread.sleep(2000000000L);


    }

    public static void main(String[] args) {

    }


}
