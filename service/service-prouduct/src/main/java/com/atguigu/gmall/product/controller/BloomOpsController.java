package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.constant.SysRedisConst;
import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.product.bloom.BloomDataQueryService;
import com.atguigu.gmall.product.bloom.BloomOpsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/product")
public class BloomOpsController{
//    布隆过滤操作
    @Autowired
BloomOpsService bloomOpsService;

    @Autowired
    BloomDataQueryService bloomDataQueryService;
    @GetMapping("/rebuild/sku/now")
    public Result rebuildBloom(){

        String bloomName = SysRedisConst.BLOOM_SKUID;
        bloomOpsService.rebuildBloom(bloomName,bloomDataQueryService);
        return Result.ok();
    }
}
