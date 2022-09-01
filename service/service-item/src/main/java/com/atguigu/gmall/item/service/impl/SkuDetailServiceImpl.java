package com.atguigu.gmall.item.service.impl;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.item.feign.SkuDetailFeignClient;
import com.atguigu.gmall.item.service.SkuDetailService;
import com.atguigu.gmall.model.product.SkuImage;
import com.atguigu.gmall.model.product.SkuInfo;
import com.atguigu.gmall.model.product.SpuSaleAttr;
import com.atguigu.gmall.model.to.CategoryViewTo;
import com.atguigu.gmall.model.to.SkuDetailTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;

@Service
public class SkuDetailServiceImpl implements SkuDetailService {


    @Autowired
    SkuDetailFeignClient skuDetailFeignClient;

    @Autowired
    ThreadPoolExecutor executor;//可配置的线程池，可自动注入

    @Override
    public SkuDetailTo getSkuDetail(Long skuId) {

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

         CompletableFuture.allOf(imageFuture,skuValueJsonFuture, priceFuture,
                 attrAndValueFuture, categoryViewFuture).join();

        return detailTo;
    }
}
