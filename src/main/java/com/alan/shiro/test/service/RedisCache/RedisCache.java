package com.alan.shiro.test.service.RedisCache;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.*;

@Component
public class RedisCache<K,V> implements Cache<K,V> {

    private static Logger logger = LoggerFactory.getLogger(RedisCache.class);

    @Autowired
    private RedisManager redisManager;
    private String keyPrefix = "shiro-cache:";
    private int expire = 0;

    @Override
    public V get(K key) throws CacheException {
        System.out.println("从缓存中去数据");
        logger.debug("get key [{}]",key);

        if (key == null) {
            return null;
        }

        try {
            String redisCacheKey = getRedisCacheKey(key);
            Object rawValue = redisManager.get(redisCacheKey);
            if (rawValue == null) {
                return null;
            }
            V value = (V) rawValue;
            return value;
        } catch (Exception e) {
            throw new CacheException(e);
        }
    }

    @Override
    public V put(K key, V value) throws CacheException {
        logger.debug("put key [{}]",key);
        System.out.println("put key [{"+key+"}]");
        if (key == null) {
            logger.warn("Saving a null key is meaningless, return value directly without call Redis.");
            return value;
        }
        try {
            String redisCacheKey = getRedisCacheKey(key);
            redisManager.set(redisCacheKey, value != null ? value : null, expire);
            return value;
        } catch (Exception e) {
            throw new CacheException(e);
        }
    }

    @Override
    public V remove(K key) throws CacheException {
        logger.debug("remove key [{}]",key);
        if (key == null) {
            return null;
        }
        try {
            String redisCacheKey = getRedisCacheKey(key);
            Object rawValue = redisManager.get(redisCacheKey);
            V previous = (V) rawValue;
            redisManager.del(redisCacheKey);
            return previous;
        } catch (Exception e) {
            throw new CacheException(e);
        }
    }


    public String getRedisCacheKey(K key){
        return this.keyPrefix+key.toString();
    }




    @Override
    public void clear() throws CacheException {
    }

    @Override
    public int size() {
        Long longSize = 0L;
        try {
            longSize = new Long(redisManager.scanSize(this.keyPrefix + "*"));
        } catch (Exception e) {
            logger.error("get keys error", e);
        }
        return longSize.intValue();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Set<K> keys() {
        Set<Session> keys = null;
        try {
            keys = redisManager.scan(this.keyPrefix + "*");
        } catch (Exception e) {
            logger.error("get keys error", e);
            return Collections.emptySet();
        }

        if (CollectionUtils.isEmpty(keys)) {
            return Collections.emptySet();
        }

        Set<K> convertedKeys = new HashSet<K>();
        for (Session key:keys) {
            try {
                convertedKeys.add((K) key);
            } catch (Exception e) {
                logger.error("deserialize keys error", e);
            }
        }
        return convertedKeys;
    }

    @Override
    public Collection<V> values() {
        Set<Session> keys = null;
        try {
            keys = redisManager.scan(this.keyPrefix + "*");
        } catch (Exception e) {
            logger.error("get values error", e);
            return Collections.emptySet();
        }

        if (CollectionUtils.isEmpty(keys)) {
            return Collections.emptySet();
        }

        List<V> values = new ArrayList<V>(keys.size());
        for (Session key : keys) {
            V value = null;
            try {
                value = (V) redisManager.get(key.getId().toString());
            } catch (Exception e) {
                logger.error("deserialize values= error", e);
            }
            if (value != null) {
                values.add(value);
            }
        }
        return Collections.unmodifiableList(values);
    }

}
