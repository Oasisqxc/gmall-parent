package com.atguigu.gmall.product.bloom.impl;

import com.atguigu.gmall.product.bloom.BloomDataQueryService;
import com.atguigu.gmall.product.bloom.BloomOpsService;
import com.atguigu.gmall.product.service.SkuInfoService;
import org.apache.http.nio.entity.NByteArrayEntity;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BloomOpsServiceImpl implements BloomOpsService{

    @Autowired
    RedissonClient redissonClient;

    @Autowired
    SkuInfoService skuInfoService;
    @Override
    public void rebuildBloom(String bloomName, BloomDataQueryService bloomDataQueryService) {

        RBloomFilter<Object> oldBloomFilter = redissonClient.getBloomFilter(bloomName);
//        1.先准备一个新的初始化好的布隆过滤器
        String newBloomName=bloomName+"_new";
        RBloomFilter<Object> bloomFilter = redissonClient.getBloomFilter(newBloomName);
//        2.拿到所有商品id
//        List<Long> allSkuId = skuInfoService.findAllSkuId();
        List list = bloomDataQueryService.queryData();
        for (Object skuId : list) {
            bloomFilter.add(skuId);
        }
//        3.初始化新的布隆
         bloomFilter.tryInit(1000000,0.00001);
//        4.新布隆就绪
//        ob bb nb

//        5.两个交换 ob要变成nb ob先转为bb 然后让nb转为ob
          oldBloomFilter.renameAsync("bb");//老布隆下线
          bloomFilter.renameAsync(bloomName);//新布隆上线

//        6.删除老布隆ob和中间交换层bb
        oldBloomFilter.deleteAsync();
        redissonClient.getBloomFilter("bb").deleteAsync();

    }
}
