package com.code_review_backend.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class CacheService {

    private final RedisTemplate<String, Object> redisTemplate;
    private static final long TTL_HOURS = 24;

    /**
     * Generates a unique key based on language and a hash of the code.
     * We hash the code to prevent extremely long keys in Redis.
     */

    public String generateKey(String language, String code) {
        String codeHash = DigestUtils.sha256Hex(code);
        return String.format("reviseai:review:%s:%s", language.toLowerCase(), codeHash);
    }

    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void put(String key, Object value) {
        redisTemplate.opsForValue().set(key, value, TTL_HOURS, TimeUnit.HOURS);
    }
}