package com.ec.singleOut.service;

import com.ec.singleOut.core.TaskCallBack;
import com.ec.singleOut.entity.CrmDetailEntity;
import com.ec.singleOut.entity.CrmLoseRuleEntity;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by ecuser on 2015/12/28.
 */
@Component
public interface  DiaodanService {


    /**
     *  根据企业id，查询该企业的掉单规则
     * @param corpId
     * @return
     */

    public CrmLoseRuleEntity searcherRulesByCorpId(long corpId);


    /**
     * 根据rules表中查询所有设置掉单规则的企业id
     *
     */
    public List<CrmLoseRuleEntity> searcherSetDiaodanCorpIds();


    /**
     *根据企业id查询，查询即将掉单的crmid，并入库
     * @param  timeInterval　离掉单日期前间隔多久时间
     *                     timeInterval ==0  表示已经掉单了
     *
     */
    public List<CrmDetailEntity> searcherWillDeadLineDiaodanCrmIdByCorpId(int timeInterval,long corpid);


    /**
     * 处理企业下的已经掉单的客户
     * 处理逻辑
     * 1.更新db
     * 2.同时更新es
     * 3.写轨迹操作
     */
    public void dealDeadLineDiaodanCrmIdByCorpId(long corpid);


    /**
     * 处理掉单失败的企业id 下crmids，
     * @param corpid
     * @param crmIds
     * @param stage 表示上一次调单执行到的阶段
     */
    public void dealFail2DeadLineCrmId(long corpid, List<Long> crmIds, int stage)throws InterruptedException;


    /**
     * 批量处理逻辑
     * 1.更新db
     * 2.同时更新es
     * 3.写轨迹操作
     */
    public void dealDeadLineDiaodanCrmIdsByCorpIds(List<CrmLoseRuleEntity> corpids,TaskCallBack callBack);


    /**
     * 暴露的dubobofuw
     */
    public void dealDeadLineDiaodan();




    public void   dealWillLoseDan();






    /**
     *
     */
    public void dealWillDeadLineDaoDan(TaskCallBack taskCallBack);

    /**
     * 查询在crmDetail表里，而不在contactTime表里面
     * @param corpId
     * @return
     */
    public List<CrmDetailEntity> findNotIncontactTimeTableCrmId(long corpId,String effectiveType);




}
