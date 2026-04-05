package com.example.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class RedisUtil {
    private final RedisTemplate<String, Object> redisTemplate;

    public void setBlackList(String key, Object o, Long milliSeconds) {
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(o.getClass()));
        redisTemplate.opsForValue().set(key, o, milliSeconds, TimeUnit.MILLISECONDS);
    }

    public Object getBlackList(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public boolean deleteBlackList(String key) {
        return redisTemplate.delete(key);
    }

    public boolean hasKeyBlackList(String key) {
        return redisTemplate.hasKey(key);
    }
}
