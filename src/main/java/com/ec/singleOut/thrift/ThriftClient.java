/**
 * @Package com.ec.nsqc.thrift
 * @Description: TODO
 * @author ecuser
 * @date 2015年7月14日 下午5:51:51
 */
package com.ec.singleOut.thrift;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @ClassName: ThriftClient
 * @Description: TODO
 * @author ecuser
 * @date 2015年7月14日 下午5:51:51
 */
@Component
public class ThriftClient {

    private static final Logger logger = LogManager.getLogger(ThriftClient.class);
    private static final Logger thriftLog = LogManager.getLogger(ThriftClient.class);
    private static final int BATCH_SIZE = 200;

    /** 连接提供池 */
    @Autowired
    private ConnectionProvider connectionProvider;

    public void notifyES(long corpId, List<Long> crmIds, int repeatTime) throws Exception {
        TSocket socket = null;

        try {
            socket = connectionProvider.getConnection();
            TFramedTransport tff = new TFramedTransport(socket);
            TProtocol protocol = new TBinaryProtocol(tff);
            EsProxyService.Client client = new EsProxyService.Client(protocol);

                    client.batchInsertOrUpdate(corpId, crmIds);
        } catch (Exception e) {
            if (socket != null && socket.isOpen()) {
                socket.close();
            }
            logger.warn("连接ES失败！corpId=" + corpId + ", size=," + crmIds.size() + ", repeatTime=" + (4 - repeatTime));
            if (repeatTime == 0) {
                thriftLog.error("连接ES重试三次后失败！corpId=" + corpId + ", size=" + crmIds.size(), e);
                throw e;
            } else {
                repeatTime--;
                notifyES(corpId, crmIds, repeatTime);
            }
        } finally {
            if (socket != null) {
                connectionProvider.returnCon(socket);
            }
        }
    }

}
