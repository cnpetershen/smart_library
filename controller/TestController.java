package com.library.smart.controller;

import com.library.smart.common.Result;
import com.library.smart.utils.RedisCache;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class TestController {

    private final RedisCache redisCache;

    @GetMapping("/hello")
    public Result<String> hello() {
        return Result.success("智慧图书馆后端启动成功");
    }

    @GetMapping("/")
    public Result<String> index() {
        return Result.success("智慧图书馆 API 服务运行中");
    }

    @GetMapping("/redis")
    public Result<String> testRedis() {
        String key = "test:redis:ping";
        String value = "pong";
        redisCache.set(key, value);
        String result = redisCache.get(key, String.class);
        if (value.equals(result)) {
            return Result.success("Redis 测试成功: " + result);
        }
        return Result.success("Redis 连接异常");
    }
}
