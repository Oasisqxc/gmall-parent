package com.atguigu.gmall.item.service.impl;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.common.util.Jsons;
import com.atguigu.gmall.item.feign.SkuDetailFeignClient;
import com.atguigu.gmall.item.service.SkuDetailService;
import com.atguigu.gmall.model.product.SkuImage;
import com.atguigu.gmall.model.product.SkuInfo;
import com.atguigu.gmall.model.product.SpuSaleAttr;
import com.atguigu.gmall.model.to.CategoryViewTo;
import com.atguigu.gmall.model.to.SkuDetailTo;
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
            Result<List<SkuImage>> skuImages = skuDetailFeignClient.getSkuImages(skuId);
            skuInfo.setSkuImageList(skuImages.getData());
        }, executor);

        //        3.查询销售属性名和值
        CompletableFuture<Void> attrAndValueFuture = skuInfoFuture.thenAcceptAsync(skuInfo -> {
            Result<List<SpuSaleAttr>> skuSalAttrValues =
                    skuDetailFeignClient.getSkuSalAttrValues(skuId, skuInfo.getSpuId());
            detailTo.setSpuSaleAttrList(skuSalAttrValues.getData());
        }, executor);
//        4.查询商品实时价格
        CompletableFuture<Void> priceFuture = CompletableFuture.runAsync(() -> {
            Result<BigDecimal> sku1010Price = skuDetailFeignClient.getSku1010Price(skuId);
            detailTo.setPrice(sku1010Price.getData());
        }, executor);

//        5.查sku组合
        CompletableFuture<Void> skuValueJsonFuture = skuInfoFuture.thenAcceptAsync(skuInfo -> {
            Result<String> skuValueJson = skuDetailFeignClient.
                    getSkuValueJson(skuInfo.getSpuId());
            detailTo.setValuesSkuJson(skuValueJson.getData());
        }, executor);

        //    6.根据三级分类id查询分类
        CompletableFuture<Void> categoryViewFuture = skuInfoFuture.thenAcceptAsync(skuInfo -> {
            Result<CategoryViewTo> categoryView = skuDetailFeignClient
                    .getCategoryView(skuInfo.getCategory3Id());
            detailTo.setCategoryView(categoryView.getData());
        }, executor);

        CompletableFuture.allOf(imageFuture, skuValueJsonFuture, priceFuture,
                attrAndValueFuture, categoryViewFuture).join();

        return detailTo;
    }

    @Override
    public SkuDetailTo getSkuDetail(Long skuId) {
//        1.先看缓存中有没有数据 sku:info:55

        String stringInfo = redisTemplate.opsForValue().get("sku:info:" + skuId);
       if ("x".equals(stringInfo)){
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
