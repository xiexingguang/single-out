package com.ec.singleOut.core;

import com.alibaba.fastjson.JSON;
import com.ec.singleOut.bean.LoseCrmId;
import com.ec.singleOut.service.DiaodanService;
import com.ec.singleOut.util.DateUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by jasshine_xxg on 2016/1/3.
 */
@Component
public class ScheduleCrm {

    private final Logger LOG = LogManager.getLogger(ScheduleCrm.class);
    private static ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
    private static ExecutorService  threadPoolExecutor = null;
    @Autowired
    private DiaodanService diaodanService;

    public void start() {
        LOG.info("...................ScheduleCrm  established  success..............");
        scheduledExecutorService.scheduleWithFixedDelay(new ScanFail2ExecuteLose(), 10, 5, TimeUnit.SECONDS);

        if (threadPoolExecutor == null) {
            threadPoolExecutor = Executors.newSingleThreadExecutor();
        }

    }

    class ScanFail2ExecuteLose implements Runnable {

        public void run() {
            BlockingQueue<LoseCrmId> loseCrmIds = SingleOutContext.FAIL_TO_EXCUTE_LOSECRM;
            LOG.info("scan blockQueue ,the queue size is  ============>" + loseCrmIds.size() + "=========scan time is  ===========> " + DateUtil.convertDate2String(new Date()));
            try {
                final LoseCrmId loseCrmId = loseCrmIds.take(); //
                LOG.info("take loseCrmId from queue....., the lsoeCrmId is ============>" + JSON.toJSONString(loseCrmId));
                final long corpId = loseCrmId.getCorpId();
                final List<Long> crmIds = loseCrmId.getCrmIds();
                final  int stage = loseCrmId.getStage();
                threadPoolExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            diaodanService.dealFail2DeadLineCrmId(corpId, crmIds, stage);  // 起线程池去跑
                        } catch (Exception e) {
                             LOG.error("fail to schedule deal lose crm:  the lose Crim json is :" + JSON.toJSONString(loseCrmId));
                        }
                    }
                });
            } catch (Exception e) {
                LOG.error("fail to take crmids from queue",e);
            }
        }
    }

    public void stop() {
        try {
            scheduledExecutorService.shutdownNow();
            threadPoolExecutor.shutdownNow();
        } catch (Exception e) {
            LOG.error("fail to stop the scheduled..",e);
        }
    }
}
