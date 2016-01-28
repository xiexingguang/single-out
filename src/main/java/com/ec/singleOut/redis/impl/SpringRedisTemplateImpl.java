/**
 * @Package com.ec.usercenter.redis.template.impl
 * @Description: TODO
 * @author Administrator
 * @date 2015年11月10日 上午9:19:22
 */
package com.ec.singleOut.redis.impl;

import com.ec.singleOut.redis.RedisTemplate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName: SpringRedisTemplateImpl
 * @Description: TODO
 * @author Administrator
 * @date 2015年11月10日 上午9:19:22
 */
@Repository
public class SpringRedisTemplateImpl implements RedisTemplate {

    private static final Logger log = LogManager.getLogger(SpringRedisTemplateImpl.class);

    @Resource(name = "springRedisTemplate")
    private org.springframework.data.redis.core.RedisTemplate<String, String> redisTemplate;
    @Resource(name = "springRedisTemplate1")
    private org.springframework.data.redis.core.RedisTemplate<String, String> redisTemplate1;

    @Resource(name = "springRedisTemplate")
    private ValueOperations<String, String> valueOperations;
    @Resource(name = "springRedisTemplate1")
    private ValueOperations<String, String> valueOperations1;

    @Resource(name = "springRedisTemplate")
    private HashOperations<String, String, String> hashOperations;
    @Resource(name = "springRedisTemplate1")
    private HashOperations<String, String, String> hashOperations1;


    public org.springframework.data.redis.core.RedisTemplate<String, String> getRedisTemplate(long corpId) {
        if (corpId % 2 == 0) {
            return redisTemplate;
        }
        return redisTemplate1;
    }

    public HashOperations<String, String, String> getHashOperations(long corpid) {
        if (corpid % 2 == 0) {
            return hashOperations;
        }
        return hashOperations1;
    }

    public ValueOperations<String, String> getValueOperations(long corpid) {
        if (corpid % 2 == 0) {
            return valueOperations;
        }
        return valueOperations1;
    }



    private void setValue(org.springframework.data.redis.core.RedisTemplate<String, String> redisTemplate, final byte[] key, final byte[] value, final long liveTime) {
        redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                connection.set(key, value);
                if (liveTime > 0) {
                    connection.expire(key, liveTime);
                }
                return 1L;
            }
        });
    }


    @Override
    public void set(long corpId, String key, String value, int liveTime) {
        this.setValue(getRedisTemplate(corpId), key.getBytes(), value.getBytes(), liveTime);
    }





    @Override
    public void set(String key, String value,long corpid) {
        try {
            valueOperations.set(key, value);

        } catch (Throwable t) {

        }
    }


    @Override
    public String get(String key,long corpid) {
        String value = null;
        try {
            value = getValueOperations(corpid).get(key);

        } catch (Throwable t) {

        }
        return value;
    }

    @Override
    public Long del(String key) {
        try {
            redisTemplate.delete(key);

        } catch (Throwable t) {

        }
        return null;
    }

    @Override
    public Long hset(String key, String hashKey, String value) {
        try {
            hashOperations.put(key, hashKey, value);

        } catch (Throwable t) {

        }
        return null;
    }

    @Override
    public String hget(String key, String hashKey) {
        String value = null;
        try {
            value = hashOperations.get(key, hashKey);

        } catch (Throwable t) {

        }
        return value;
    }

    @Override
    public List<String> hgetAll(String key) {
        List<String> values = null;
        try {
            values = hashOperations.values(key);

        } catch (Throwable t) {

        }
        return values;
    }

    @Override
    public Long hdel(String key, String... hashKeys) {
        try {
            hashOperations.delete(key, (Object[]) hashKeys);

        } catch (Throwable t) {

        }
        return null;
    }

    @Override
    public Long expire(String key, long timeoutSecond) {
        try {
            redisTemplate.expire(key, timeoutSecond, TimeUnit.SECONDS);

        } catch (Throwable t) {

        }
        return null;
    }


}
