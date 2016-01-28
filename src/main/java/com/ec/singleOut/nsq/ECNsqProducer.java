package com.ec.singleOut.nsq;


import com.ec.singleOut.properties.NsqProperties;
import com.trendrr.nsq.BatchCallback;
import com.trendrr.nsq.NSQProducer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by ecuser on 2015/12/17.
 */

@Component
public class ECNsqProducer {

    private static final Logger LOG = LogManager.getLogger(ECNsqProducer.class);
    @Autowired
    private NsqProperties nsqProperties;

    public static NSQProducer producer;


    public  NSQProducer getBatchProducer(String topic, int batchSize) {

            LOG.info("初始化nsq生产者...topic=" + topic + "host=" + nsqProperties.nsqdHost + "   port= " + nsqProperties.port + " threadNum= " + nsqProperties.producerNum);

        if (producer == null) {
            producer = new NSQProducer();
        } else {
            return producer;
        }
            producer.addAddress(nsqProperties.nsqdHost , nsqProperties.port, nsqProperties.producerNum);
            producer.configureBatch(topic, new BatchCallback() {
                    public void batchSuccess(String topic, int num) {
                    }

                    public void batchError(Exception ex, String topic, List<byte[]> messages) {
                        LOG.error("初始化nsq生产者失败!", ex);
                    }
            }, Integer.valueOf(batchSize), (Long) null, (Integer) null);
            producer.start();
            LOG.debug("初始化nsq生产者成功...");
            return producer;

    }


    public void close() {
        try {
            producer.close();

        } catch (Exception e) {
            LOG.error("fail to close the nsqProducer",e);
        }
    }

}
