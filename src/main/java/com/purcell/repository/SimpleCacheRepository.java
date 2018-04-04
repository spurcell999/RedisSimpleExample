package com.purcell.repository;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Component("simpleCacheRepository")
public class SimpleCacheRepository implements ICacheRepository<String, String> {

    @Autowired
    @Qualifier("stringRedisTemplate")
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public void put(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    @Override
    public void multiPut(Map<String, String> keyValues) {
        redisTemplate.opsForValue().multiSet(keyValues);
    }

    public String get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public List<String> multiGet(Collection<String> keys) {
        return redisTemplate.opsForValue().multiGet(keys);
    }

    public void delete(String key) {
        redisTemplate.delete(key);

    }
}
