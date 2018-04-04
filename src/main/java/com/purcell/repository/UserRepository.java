package com.purcell.repository;


import com.purcell.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component("userRepository")
public class UserRepository implements IHashRepository<User> {
    @Autowired
    @Qualifier("redisTemplate")
    private RedisTemplate<String,User> redisTemplate;

    @Override
    public void put(User user) {
        redisTemplate.opsForHash().put(User.OBJECT_KEY, user.getId(), user);
    }

    @Override
    public void multiPut(Collection<User> keys) {
        Map<Long,User> keyValues = new HashMap<Long, User>(keys.size());
        for(User user : keys)
        {
            keyValues.put(new Long(user.getId()), user);
        }
        redisTemplate.opsForHash().putAll(User.OBJECT_KEY, keyValues);
    }

    @Override
    public User get(User key) {
        return (User) redisTemplate.opsForHash().get(User.OBJECT_KEY,
                key.getId());
    }

    @Override
    public List<User> multiGet(Collection<User> users) {
        List<Object> keys = new ArrayList<Object>(users.size());
        for(User user : users)
        {
            keys.add(user.getId());
        }
        return (List<User>)(List<?>)redisTemplate.opsForHash().multiGet(User.OBJECT_KEY, keys);
    }

    @Override
    public void delete(User key) {
        redisTemplate.opsForHash().delete(User.OBJECT_KEY, key.getId());
    }

    @Override
    public List<User> getObjects() {
        List<User> users = new ArrayList<User>();
        for (Object user : redisTemplate.opsForHash().values(User.OBJECT_KEY) ){
            users.add((User) user);
        }
        return users;
    }

    @Override
    public void delete() {
        redisTemplate.delete(User.OBJECT_KEY);
    }
}