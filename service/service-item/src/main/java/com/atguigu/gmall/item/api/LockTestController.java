package com.atguigu.gmall.item.api;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.item.lock.RedisDistLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RequestMapping("/lock")
@RestController
public class LockTestController {

    @Autowired
    StringRedisTemplate redisTemplate;

    @Resource
    RedisDistLock redisDistLock;


    @GetMapping("/incr") //1w请求
    public Result increment(){

        String token = redisDistLock.lock();
//        阻塞式加锁
//        获取值
        System.out.println("a");
        String a = redisTemplate.opsForValue().get("a");
        int i = Integer.parseInt(a);
//        业务计算值
        i++;
//        保存值
        redisTemplate.opsForValue().set("a",i+""); //1
        redisDistLock.unlock(token);//分布式锁
        return Result.ok();
    }


}
