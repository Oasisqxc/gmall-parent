package com.atguigu.gmall.item.feign;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.SkuImage;
import com.atguigu.gmall.model.product.SkuInfo;
import com.atguigu.gmall.model.product.SpuSaleAttr;
import com.atguigu.gmall.model.to.CategoryViewTo;
import com.atguigu.gmall.model.to.SkuDetailTo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.util.List;

@RequestMapping("/api/inner/rpc/product")
@FeignClient("service-product")
public interface SkuDetailFeignClient {

//    @GetMapping("/skudetail/{skuId}")
//    public Result<SkuDetailTo> getDetail(@PathVariable("skuId") Long skuId);

    //   1. 查询skuinfo信息
    @GetMapping("/skudetail/info/{skuId}")
    public Result<SkuInfo> getSkuInfo(@PathVariable("skuId") Long skuId);


    //    2.查询商品图片信息
    @GetMapping("/skudetail/images/{skuId}")
    public Result<List<SkuImage>> getSkuImages(@PathVariable("skuId") Long skuId);


    //    3.获取商品的实时价格

    @GetMapping("/skudetail/price/{skuId}")
    public Result<BigDecimal> getSku1010Price(@PathVariable("skuId") Long skuId);

    //    4.查询销售属性名和值
    @GetMapping("/skudetail/saleattrvalues/{skuId}/{spuId}")
    public Result<List<SpuSaleAttr>> getSkuSalAttrValues(@PathVariable("skuId")
                                                                 Long skuId,
                                                         @PathVariable("spuId")
                                                                 Long spuId);

    //    5.查询sku组合 valueJson
    @GetMapping("/skudetail/valueJson/{spuId}")
    public Result<String> getSkuValueJson(
            @PathVariable("spuId")
                    Long spuId);

    //    6.根据三级分类id查询分类
    @GetMapping("/skudetail/categoryview/{c3Id}")
    public Result<CategoryViewTo> getCategoryView(@PathVariable("c3Id") Long c3Id);
}
