package com.atguigu.gmall.product.init;

import com.atguigu.gmall.common.constant.SysRedisConst;
import com.atguigu.gmall.product.service.SkuInfoService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

/*
* 容器启动成功后，连上数据库，查到所有商品id，在布隆里面进行占位
* */
@Service
@Slf4j
public class SkuIdBloomInitService {

    @Autowired
    RedissonClient redissonClient;

    @Autowired
    SkuInfoService skuInfoService;
//项目一启动就运行
    @PostConstruct //当前组件对象创建成功以后
    public void initSkuBloom(){
log.info("布隆初始化正在运行-------------");
//1.查出所有的skuid
       List<Long>  skuIds = skuInfoService.findAllSkuId();
//        2.把所有的skuid初始化到布隆过滤器中
        RBloomFilter<Object> bloomFilter =
                redissonClient.getBloomFilter(SysRedisConst.BLOOM_SKUID);


//        3.初始化布隆过滤器
        boolean exists = bloomFilter.isExists();
        if (!exists){
            //尝试初始化。如果布隆过滤器没有初始化过，就尝试初始化
            /*
            *
            * long expectedInsertions,期望插入的数据量
            *  double falseProbability,误判率
            * */
            bloomFilter.tryInit(1000000,0.00001);

        }

//        4.把所有的商品添加到布隆中，不害怕某个微服务把这个事情做失败
        for (Long skuId : skuIds) {
            bloomFilter.add(skuId);
        }

        log.info("布隆初始化完成.....,总计添加了{}条数据",skuIds.size());
    }
}
