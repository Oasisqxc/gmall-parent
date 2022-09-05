package com.atguigu.gmall.product.schedule;

import com.atguigu.gmall.common.constant.SysRedisConst;
import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.product.bloom.BloomDataQueryService;
import com.atguigu.gmall.product.bloom.BloomOpsService;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
//重建布隆任务
public class RebuildBloomTask {

    @Autowired
    BloomOpsService bloomOpsService;

@Autowired
    BloomDataQueryService bloomDataQueryService;
    //每隔7天重建一次； bitmap（更符合sku的场景）  88
    // * * * * * ? *
    // 秒 分 时 日 月 周 年
@Scheduled(cron = "* * 3 ? * 3")
public void rebuild(){

    bloomOpsService.rebuildBloom(SysRedisConst.BLOOM_SKUID,bloomDataQueryService);
}


}
