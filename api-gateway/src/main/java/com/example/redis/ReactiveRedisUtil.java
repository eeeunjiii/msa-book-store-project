package com.example.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ReactiveRedisUtil {

    private final ReactiveRedisTemplate<String, String> redisTemplate;

    public Mono<Boolean> hasKeyBlackList(String key) {
        return redisTemplate.hasKey(key);
    }
}
