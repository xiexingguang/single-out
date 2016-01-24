package com.ec.singleOut.nsq;

import com.alibaba.fastjson.JSON;
import com.ec.singleOut.dao.NsqTaskDao;
import com.ec.singleOut.entity.NsqTaskEntity;
import com.ec.singleOut.json.WriteNsqJson;
import com.ec.singleOut.properties.NsqProperties;
import com.trendrr.nsq.NSQProducer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ecuser on 2015/12/30.
 */
@Component
public class NsqHandler {

    private final Logger LOG = LogManager.getLogger(NsqHandler.class);

    @Autowired
    private NsqTaskDao nsqTaskDao;

    @Autowired
    private NsqProperties nsqProperties;
    @Autowired
    private ECNsqProducer ecNsqProducer;

    public void writeCustomerTrail(List<WriteNsqJson> json,long corpId) {

       if (json == null) {
        LOG.warn("the writeNsqjson was null...");
        return;
      }

        final String topic = nsqProperties.topic;
        final int nsqBatchSize = nsqProperties.nsqBatchSize;
        NSQProducer nsqProducer = ecNsqProducer.getBatchProducer(topic, nsqBatchSize);

        try {
            for (WriteNsqJson writeNsqJson : json) {
                LOG.info("send customer trail to the nsq ,the trail is :" + JSON.toJSONString(writeNsqJson));
                nsqProducer.produceBatch(topic, JSON.toJSONString(writeNsqJson).getBytes());
            }
            nsqProducer.flushBatches();
        } catch (Exception e) {
            LOG.error("fail to send customer tail ,the count of the nsqmessage size is :" + json.size(), e);
            long now = System.currentTimeMillis();
            String timeStamp = String.valueOf(now).substring(0, 10);
            List<NsqTaskEntity> nsqTaskEntities = new ArrayList<NsqTaskEntity>();
            for (WriteNsqJson writeNsqJson : json) {
                LOG.error("error customer tail  :" + JSON.toJSONString(writeNsqJson));
                NsqTaskEntity taskEntity = new NsqTaskEntity();
                taskEntity.setF_corp_id(corpId);
                taskEntity.setF_from(2);
                taskEntity.setF_topic_name(topic);
                taskEntity.setF_message(JSON.toJSONString(writeNsqJson));
                taskEntity.setF_create_time(Integer.valueOf(timeStamp));
                taskEntity.setF_user_id(0);
                nsqTaskEntities.add(taskEntity);
            }
            nsqTaskDao.batchSaveNsqTaskDao(nsqTaskEntities);
            LOG.error("batch save fail customer trail to mysql. the size is :" +json.size() );
        }

    }

}
