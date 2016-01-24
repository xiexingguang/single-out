/**
 * @Package com.ec.dao.impl
 * @Description: TODO
 * @author ecuser
 * @date 2015年5月29日 上午10:01:49
 */
package com.ec.singleOut.dao;

import net.rubyeye.xmemcached.MemcachedClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @ClassName: MemcachedDaoImpl
 * @Description: TODO
 * @author ecuser
 * @date 2015年5月29日 上午10:01:49
 */
@Component
public class MemcachedDao  {

    @Autowired
    private MemcachedClient memcachedClient;

    /*
     * <p>Title: clearUpMemcacheGroupNum</p> <p>Description: </p>
     * 
     * @param key
     * 
     * @param value
     * 
     * @see com.ec.dao.MemcachedDao#clearUpMemcacheGroupNum(long)
     */

    public void updateValueByKey(String key, int expireTime, Object value) throws Exception {
        System.out.println("clent=================>"+memcachedClient);
        memcachedClient.replace(key, expireTime, value);
    }

    /*
     * <p>Title: deleteByKey</p> <p>Description: </p>
     * 
     * @param key
     * 
     * @throws Exception
     * 
     * @see com.ec.dao.MemcachedDao#deleteByKey(java.lang.String)
     */

    public void deleteByKey(String key) throws Exception {
        memcachedClient.delete(key);
    }

}
