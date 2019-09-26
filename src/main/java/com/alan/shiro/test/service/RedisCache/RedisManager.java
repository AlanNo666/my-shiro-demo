package com.alan.shiro.test.service.RedisCache;

import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.SerializationUtils;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
public class RedisManager {

    @Autowired
    @Qualifier("redisCacheTemplate")
    RedisTemplate<String,Object > redisTemplate;

    public void expire(String key,long time){
        redisTemplate.expire(key,time,TimeUnit.SECONDS);
    }

    public Boolean hasKey(String key){
        return redisTemplate.hasKey(key);
    }

    public void del(String ... key){
        if(key!=null&&key.length>0){
            if(key.length==1){
                redisTemplate.delete(key[0]);
            }else{
                redisTemplate.delete(CollectionUtils.arrayToList(key));
            }
        }
    }

    public void del(Collection keys){
        redisTemplate.delete(keys);
    }

    public Object get(String key){
        return redisTemplate.opsForValue().get(key);
    }

    public void set(String key,Object value){
        redisTemplate.opsForValue().set(key,value);
    }

    /**
     *
     * @param key
     * @param value
     * @param time  时间要大于0 等于0为无限期
     */
    public void set(String key,Object  value,long time){
        if(time>0){
            redisTemplate.opsForValue().set(key,value,time,TimeUnit.SECONDS);
        }else{
            redisTemplate.opsForValue().set(key,value);
        }
    }

    public Set<Session> scan(String key){
        Set<Session> execute = this.redisTemplate.execute(new RedisCallback<Set<Session>>() {

            @Override
            public Set<Session> doInRedis(RedisConnection connection) throws DataAccessException {

                Set<Session> binaryKeys = new HashSet<>();

                Cursor<byte[]> cursor = connection.scan(new ScanOptions.ScanOptionsBuilder().match(key).count(1000).build());
                while (cursor.hasNext()) {
                    binaryKeys.add((Session)SerializationUtils.deserialize( cursor.next()));
                }
                return binaryKeys;
            }
        });
        return execute;
    }



    /**
     * 使用scan命令 查询某些前缀的key 有多少个
     * 用来获取当前session数量,也就是在线用户
     * @param key
     * @return
     */
    public Long scanSize(String key){
        long dbSize = this.redisTemplate.execute(new RedisCallback<Long>() {

            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                long count = 0L;
                Cursor<byte[]> cursor = connection.scan(ScanOptions.scanOptions().match(key).count(1000).build());
                while (cursor.hasNext()) {
                    cursor.next();
                    count++;
                }
                return count;
            }
        });
        return dbSize;
    }

}
