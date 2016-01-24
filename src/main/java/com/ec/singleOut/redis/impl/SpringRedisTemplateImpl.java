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
import org.springframework.data.redis.core.HashOperations;
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
    @Resource(name = "springRedisTemplate")
    private ValueOperations<String, String> valueOperations;
    @Resource(name = "springRedisTemplate")
    private HashOperations<String, String, String> hashOperations;

    @Override
    public String set(String key, String value) {
        try {
            valueOperations.set(key, value);

        } catch (Throwable t) {

        }
        return null;
    }

    @Override
    public String set(String key, String value, int timeoutSecond) {
        try {
            valueOperations.set(key, value, timeoutSecond, TimeUnit.SECONDS);

        } catch (Throwable t) {

        }
        return null;
    }

    @Override
    public String get(String key) {
        String value = null;
        try {
            value = valueOperations.get(key);

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
