package com.atguigu.gmall.item;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

@SpringBootTest
public class RedisTest {

    @Autowired
    StringRedisTemplate redisTemplate;

    @Test
    void testRedis(){

        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        ops.set("xixi","hello");
        System.out.println("redis保存完成");
        System.out.println(ops.get("xixi"));

    }
}
