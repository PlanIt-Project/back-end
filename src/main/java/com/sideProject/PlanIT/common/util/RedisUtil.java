package com.sideProject.PlanIT.common.util;

import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RedisUtil {
    private final RedisTemplate<String, String> redisTemplate;

    public void setRefreshToken(String refreshToken, Long member_id)
    {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        redisTemplate.delete(refreshToken);
        valueOperations.set(refreshToken, member_id.toString());
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
