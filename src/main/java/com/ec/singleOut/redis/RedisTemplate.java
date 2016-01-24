/**
 * @Package com.ec.usercenter.redis.template
 * @Description: TODO
 * @author Administrator
 * @date 2015年11月10日 上午9:18:22
 */
package com.ec.singleOut.redis;



import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @ClassName: RedisTemplate
 * @Description: TODO
 * @author Administrator
 * @date 2015年11月10日 上午9:18:22
 */
@Component
public interface RedisTemplate {

    public String set(String key, String value);

    public String set(String key, String value, int expireSecond);

    public String get(String key);

    public Long del(String key);

    public Long hset(String key, String hashKey, String value);

    public String hget(String key, String hashKey);

    public List<String> hgetAll(String key);

    public Long hdel(String key, String... hashKeys);

    public Long expire(String key, long timeoutSecond);

   // public <T> T executeInSession(SessionCallback<T> sesCallback);

}
