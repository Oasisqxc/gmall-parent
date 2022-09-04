package com.atguigu.gmall.item.service.impl;

import com.atguigu.gmall.common.constant.SysRedisConst;
import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.common.util.Jsons;
import com.atguigu.gmall.item.cache.CacheOpsService;
import com.atguigu.gmall.item.feign.SkuDetailFeignClient;
import com.atguigu.gmall.item.service.SkuDetailService;
import com.atguigu.gmall.model.product.SkuImage;
import com.atguigu.gmall.model.product.SkuInfo;
import com.atguigu.gmall.model.product.SpuSaleAttr;
import com.atguigu.gmall.model.to.CategoryViewTo;
import com.atguigu.gmall.model.to.SkuDetailTo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
@Slf4j
@Service
public class SkuDetailServiceImpl implements SkuDetailService {
    /**
     * Map作为缓存【本地缓存】：优缺点
     * 优点：
     * 缺点：
     * 1、100w的数据内存够不够
     */
//    private Map<Long, SkuDetailTo> skuCache = new ConcurrentHashMap<>();

    @Autowired
    SkuDetailFeignClient skuDetailFeignClient;

    @Autowired
    ThreadPoolExecutor executor;//可配置的线程池，可自动注入

    @Autowired
    StringRedisTemplate redisTemplate;

    @Autowired
    CacheOpsService cacheOpsService;

    //未缓存优化前 - 400/s
    public SkuDetailTo getSkuDetailFromRpc(Long skuId) {

        SkuDetailTo detailTo = new SkuDetailTo();

        CompletableFuture<SkuInfo> skuInfoFuture = CompletableFuture.supplyAsync(() -> {
//        1.查询基本信息
            Result<SkuInfo> result = skuDetailFeignClient.getSkuInfo(skuId);
            SkuInfo skuInfo = result.getData();
            detailTo.setSkuInfo(skuInfo);
            return skuInfo;
        }, executor);
//远程调用商品服务
//        Result<SkuDetailTo> skuDetail = skuDetailFeignClient.getDetail(skuId);

        //        2.查询商品图片信息
        CompletableFuture<Void> imageFuture = skuInfoFuture.thenAcceptAsync(skuInfo -> {
           if (skuInfo!=null){

               Result<List<SkuImage>> skuImages = skuDetailFeignClient.getSkuImages(skuId);
               skuInfo.setSkuImageList(skuImages.getData());
           }
        }, executor);

        //        3.查询销售属性名和值
        CompletableFuture<Void> attrAndValueFuture = skuInfoFuture.thenAcceptAsync(skuInfo -> {
            if (skuInfo!=null){

                Result<List<SpuSaleAttr>> skuSalAttrValues =
                        skuDetailFeignClient.getSkuSalAttrValues(skuId, skuInfo.getSpuId());
                detailTo.setSpuSaleAttrList(skuSalAttrValues.getData());
            }
        }, executor);
//        4.查询商品实时价格
        CompletableFuture<Void> priceFuture = CompletableFuture.runAsync(() -> {
            Result<BigDecimal> sku1010Price = skuDetailFeignClient.getSku1010Price(skuId);
            detailTo.setPrice(sku1010Price.getData());
        }, executor);

//        5.查sku组合
        CompletableFuture<Void> skuValueJsonFuture = skuInfoFuture.thenAcceptAsync(skuInfo -> {
           if (skuInfo!=null){

               Result<String> skuValueJson = skuDetailFeignClient.
                       getSkuValueJson(skuInfo.getSpuId());
               detailTo.setValuesSkuJson(skuValueJson.getData());
           }
        }, executor);

        //    6.根据三级分类id查询分类
        CompletableFuture<Void> categoryViewFuture = skuInfoFuture.thenAcceptAsync(skuInfo -> {

           if (skuInfo!=null){

               Result<CategoryViewTo> categoryView = skuDetailFeignClient
                       .getCategoryView(skuInfo.getCategory3Id());
               detailTo.setCategoryView(categoryView.getData());
           }
        }, executor);

        CompletableFuture.allOf(imageFuture, skuValueJsonFuture, priceFuture,
                attrAndValueFuture, categoryViewFuture).join();

        return detailTo;
    }


    public SkuDetailTo getSkuDetailxxxFeature(Long skuId) {
//        1.先看缓存中有没有数据 sku:info:55

        String stringInfo = redisTemplate.opsForValue().get("sku:info:" + skuId);
        if ("x".equals(stringInfo)) {
//           说明以前查过，只不过数据库没有此记录，为了避免再次回源，缓存了一个占位符
            return null;
        }

        if (StringUtils.isEmpty(stringInfo)) {
            // 没有缓存,回源
            SkuDetailTo fromRpc = getSkuDetailFromRpc(skuId);
            String catchJson = "x";
            if (fromRpc != null) {
                catchJson = Jsons.toStr(fromRpc);
//            放入缓存
                redisTemplate.opsForValue().set("sku:info:" + skuId, catchJson,
                        7, TimeUnit.DAYS);
            } else {
                redisTemplate.opsForValue().set("sku:info:" + skuId, catchJson, 30, TimeUnit.MINUTES);
            }

            return fromRpc;

        }

//       有缓存
//        把json转成指定对象
        SkuDetailTo skuDetailTo = Jsons.toObj(stringInfo, SkuDetailTo.class);

        return skuDetailTo;
    }

    @Override
    public SkuDetailTo getSkuDetail(Long skuId) {

        String cacheKey = SysRedisConst.SKU_INFO_PREFEIX + skuId;

//        1.先查缓存
        SkuDetailTo cacheData = cacheOpsService
                .getCacheData(cacheKey, SkuDetailTo.class);

//       2.判断
        if (cacheData == null) {
//            3.缓存中没有
//            4.先问布隆，是否有这个商品
            boolean contain = cacheOpsService.bloomContains(skuId);
            if (!contain) {
//             5.布隆说没有，一定没有
                log.info("[{}]商品 - 布隆判定没有，检测到隐藏的攻击风险....",skuId);
                return null;
            }

//         6.布隆说有，有可能有，就需要回源查数据
            boolean lock = cacheOpsService.tryLock(skuId);//为当前商品加自己的分布式锁，100w的49号查询
            if (lock) {
//                7.获取锁成功，回源查询远程
                SkuDetailTo fromRpc = getSkuDetailFromRpc(skuId);
                log.info("[{}]商品 缓存未命中，布隆说有，准备回源.....",skuId);
//                8.数据放缓存
                cacheOpsService.saveData(cacheKey, fromRpc);
//                9.解锁
                cacheOpsService.unlock(skuId);
                return fromRpc;
            }

//            10.没有获取到锁
            try {
                Thread.sleep(1000);
                return cacheOpsService.getCacheData(cacheKey, SkuDetailTo.class);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
//       3.2 缓存中有
        return cacheData;

    }



   /* //  使用本地缓存
    @Override
    public SkuDetailTo getSkuDetail(Long skuId) {
//1.先看缓存
        SkuDetailTo catchDeta = skuCache.get(skuId);
        if (catchDeta==null){
//            没有缓存
            SkuDetailTo fromRpc = getSkuDetailFromRpc(skuId);
            skuCache.put(skuId,fromRpc);
            return fromRpc;
        }

        return catchDeta;
    }*/
}
