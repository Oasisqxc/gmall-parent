package com.atguigu.gmall.product.api;

import com.atguigu.gmall.common.constant.SysRedisConst;
import com.atguigu.gmall.common.result.Result;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
public class HelloController {

    //属性一直在； GC，根可达
    Map<String, String> aa = new HashMap<>();

    @GetMapping("/haha/hello")
    public Result hello() {
        String s = UUID.randomUUID().toString();
        aa.put(s, s);
        return Result.ok();
    }

    @Resource
    RedissonClient redissonClient;
    @GetMapping("/bloom/contains/{skuId}")
    public Result bloomContains(@PathVariable("skuId") Long skuId){
//        1.拿到布隆过滤器
        RBloomFilter<Object> bloomFilter = redissonClient.getBloomFilter(SysRedisConst.BLOOM_SKUID);
        boolean contains = bloomFilter.contains(skuId);
        return Result.ok("布隆有吗？"+contains);
    }
}
