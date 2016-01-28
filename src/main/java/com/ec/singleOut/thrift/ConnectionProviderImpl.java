package com.ec.singleOut.thrift;

import org.apache.commons.pool.ObjectPool;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * 连接池实现
 */
@Service
public class ConnectionProviderImpl implements ConnectionProvider, InitializingBean, DisposableBean {

    /** 服务的IP地址 */
    @Value("${thrift.server.ip}")
    public String serviceIP;
    /** 服务的端口 */
    @Value("${thrift.server.port}")
    public int servicePort;
    /** 连接超时配置 */
    @Value("${thrift.server.conTimeOut}")
    public int conTimeOut;
    /** 可以从缓存池中分配对象的最大数量 */
    @Value("${thrift.server.maxActive}")
    public int maxActive = GenericObjectPool.DEFAULT_MAX_ACTIVE;
    /** 缓存池中最大空闲对象数量 */
    @Value("${thrift.server.maxIdle}")
    public int maxIdle = GenericObjectPool.DEFAULT_MAX_IDLE;
    /** 缓存池中最小空闲对象数量 */
    @Value("${thrift.server.minIdle}")
    public int minIdle = GenericObjectPool.DEFAULT_MIN_IDLE;
    /** 阻塞的最大数量 */
    @Value("${thrift.server.maxWait}")
    public long maxWait = GenericObjectPool.DEFAULT_MAX_WAIT;

    /** 从缓存池中分配对象，是否执行PoolableObjectFactory.validateObject方法 */
    @Value("${thrift.server.testOnBorrow}")
    public boolean testOnBorrow = GenericObjectPool.DEFAULT_TEST_ON_BORROW;
    @Value("${thrift.server.testOnReturn}")
    public boolean testOnReturn = GenericObjectPool.DEFAULT_TEST_ON_RETURN;
    @Value("${thrift.server.testWhileIdle}")
    public boolean testWhileIdle = GenericObjectPool.DEFAULT_TEST_WHILE_IDLE;

    /** 对象缓存池 */
    private ObjectPool objectPool = null;

    @Override
    public TSocket getConnection() {
        try {
            // 从对象池取对象
            TSocket socket = (TSocket) objectPool.borrowObject();
            return socket;
        } catch (Exception e) {
            throw new RuntimeException("error getConnection()", e);
        }
    }

    @Override
    public void returnCon(TSocket socket) {
        try {
            // 将对象放回对象池
            objectPool.returnObject(socket);
        } catch (Exception e) {
            throw new RuntimeException("error returnCon()", e);
        }
    }

    @Override
    public void destroy() throws Exception {
        try {
            objectPool.close();
        } catch (Exception e) {
            throw new RuntimeException("erorr destroy()", e);
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void afterPropertiesSet() throws Exception {
        // 对象池
        objectPool = new GenericObjectPool<TTransport>();
        //
        ((GenericObjectPool<TTransport>) objectPool).setMaxActive(maxActive);
        ((GenericObjectPool<TTransport>) objectPool).setMaxIdle(maxIdle);
        ((GenericObjectPool<TTransport>) objectPool).setMinIdle(minIdle);
        ((GenericObjectPool<TTransport>) objectPool).setMaxWait(maxWait);
        ((GenericObjectPool<TTransport>) objectPool).setTestOnBorrow(testOnBorrow);
        ((GenericObjectPool<TTransport>) objectPool).setTestOnReturn(testOnReturn);
        ((GenericObjectPool<TTransport>) objectPool).setTestWhileIdle(testWhileIdle);

        ((GenericObjectPool<TTransport>) objectPool).setWhenExhaustedAction(GenericObjectPool.WHEN_EXHAUSTED_BLOCK);
        // 设置factory
        ThriftPoolableObjectFactory thriftPoolableObjectFactory = new ThriftPoolableObjectFactory(serviceIP, servicePort, conTimeOut);
        objectPool.setFactory(thriftPoolableObjectFactory);
    }

}