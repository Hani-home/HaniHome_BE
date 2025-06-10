package org.hanihome.hanihomebe.security.auth.application.jwt.blacklist;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class BlacklistService {
    private final RedisTemplate<String, String> redisTemplate;
    public void blacklistAccessToken(String accessToken, long expirationMs) {
        redisTemplate.opsForValue().set("BLACKLIST:" + accessToken, "logout", expirationMs, TimeUnit.MILLISECONDS);
    }
}
