package com.ec.singleOut.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by ecuser on 2015/12/17.
 */
@Component
public class NsqProperties {
    @Value("${nsq.nsqdHost}")
    public String nsqdHost;
    @Value("${nsq.port}")
    public int port;
    @Value("${nsq.topic}")
    public String topic;
    @Value("${nsq.produce.batchSize}")
    public int nsqBatchSize;
    @Value("${nsq.produce.producerNum}")
    public int producerNum;

}
