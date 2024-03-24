package com.sideProject.PlanIT.common.util;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RedisUtil {

    @Value("${spring.email.redis-timeLimit}")
    private Long emailExpire;

    @Value("${spring.jwt.refresh-token-expire}")
    private Long refreshExpire;

    private final RedisTemplate<String, String> redisTemplate;


    public void setMailValidation(String email, String validationCode) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(email, validationCode, Duration.ofMillis(emailExpire));
    }
    public void setRefreshToken(String refreshToken, Long member_id)
    {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        deleteByValue(member_id.toString());
        valueOperations.set(refreshToken, member_id.toString(), Duration.ofMillis(refreshExpire));
    }

    public String getData(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void deleteData(String key) {
        redisTemplate.delete(key);
    }

    public boolean isExist(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    public void deleteByValue(String value) {
        ValueOperations<String, String> valueOps = redisTemplate.opsForValue();

        redisTemplate.keys("*").forEach(key -> {
            String storedValue = valueOps.get(key);
            if (value.equals(storedValue)) {
                redisTemplate.delete(key);
            }
        });
    }
}
