package org.hanihome.hanihomebe.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/redis")
@RequiredArgsConstructor
public class RedisTestController {

    private final StringRedisTemplate redisTemplate;

    @PostMapping("/set")
    public String setValue(@RequestParam String key, @RequestParam String value) {
        redisTemplate.opsForValue().set(key,value);
        return "저장 완료";
    }

    @GetMapping("/get")
    public String getValue(@RequestParam String key) {
        return redisTemplate.opsForValue().get(key);
    }


}
