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

@Service
public class SkuDetailServiceImpl implements SkuDetailService {

    @Autowired
    SkuDetailFeignClient skuDetailFeignClient;

    @Override
    public SkuDetailTo getSkuDetail(Long skuId) {
        SkuDetailTo detailTo = new SkuDetailTo();
//远程调用商品服务
//        Result<SkuDetailTo> skuDetail = skuDetailFeignClient.getDetail(skuId);
//        1.查询基本信息
        Result<SkuInfo> result = skuDetailFeignClient.getSkuInfo(skuId);
        SkuInfo skuInfo = result.getData();
        detailTo.setSkuInfo(skuInfo);

//        2.查询商品图片信息
        Result<List<SkuImage>> skuImages = skuDetailFeignClient.getSkuImages(skuId);
        skuInfo.setSkuImageList(skuImages.getData());
//        3.查询销售属性名和值

        Result<List<SpuSaleAttr>> skuSalAttrValues =
                skuDetailFeignClient.getSkuSalAttrValues(skuId, skuInfo.getSpuId());
        detailTo.setSpuSaleAttrList(skuSalAttrValues.getData());
//        4.查询商品实时价格
        Result<BigDecimal> sku1010Price = skuDetailFeignClient.getSku1010Price(skuId);
        detailTo.setPrice(sku1010Price.getData());
//        5.查sku组合
        Result<String> skuValueJson = skuDetailFeignClient.
                getSkuValueJson(skuInfo.getSpuId());
        detailTo.setValuesSkuJson(skuValueJson.getData());

        //    6.根据三级分类id查询分类
        Result<CategoryViewTo> categoryView = skuDetailFeignClient
                .getCategoryView(skuInfo.getCategory3Id());
        detailTo.setCategoryView(categoryView.getData());
        return detailTo;
    }
}
