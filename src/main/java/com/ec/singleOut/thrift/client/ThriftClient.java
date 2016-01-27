package com.ec.singleOut.thrift.client;

import com.ec.singleOut.properties.ThriftProperties;
import com.ec.singleOut.thrift.api.EsProxyService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by ecuser on 2015/12/31.
 */
@Component
public class ThriftClient {

    private static TTransport tTransport;
    private static EsProxyService.Client client;
    @Autowired
    private ThriftProperties thriftProperties;
    private static final Logger LOG = LogManager.getLogger(ThriftClient.class);

    public  EsProxyService.Client getThriftClient()  {
        LOG.info("begin to connect the thrift client ");
        try {
            if (client == null) {
                if (tTransport == null) {
                    TSocket tSocket = new TSocket(thriftProperties.host, thriftProperties.port, 30 * 1000); //读写超时时间10秒
                    tTransport = new TFramedTransport(tSocket);
                }
                tTransport.open();
                TProtocol protocol = new TBinaryProtocol(tTransport);
                client = new EsProxyService.Client(protocol);
            }
        } catch (Exception e) {
              LOG.error("fail to connect the thrift client ,the exceptions is  ",e);
            return null;
        }

        if(!tTransport.isOpen()) {
            try {
                tTransport.open();
            } catch (Exception e) {
                LOG.error("fail to open Transport ",e);
                client = null;
            }
        }

        return client;
    }

    public  void close() {
        try {
            tTransport.close();
        } catch (Exception e) {
            LOG.error("fail to close the thrift client");
        }
        LOG.info("close the thrift client");
    }

    public boolean  isOpen() {
        if (tTransport == null) {
            return false;
        }
        return tTransport.isOpen();
    }

}
