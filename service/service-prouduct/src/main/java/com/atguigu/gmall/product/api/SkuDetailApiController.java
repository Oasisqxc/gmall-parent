package com.atguigu.gmall.product.api;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.SkuImage;
import com.atguigu.gmall.model.product.SkuInfo;
import com.atguigu.gmall.model.product.SpuSaleAttr;
import com.atguigu.gmall.model.to.CategoryViewTo;
import com.atguigu.gmall.model.to.SkuDetailTo;
import com.atguigu.gmall.product.mapper.BaseCategory3Mapper;
import com.atguigu.gmall.product.service.BaseCategory3Service;
import com.atguigu.gmall.product.service.SkuInfoService;
import com.atguigu.gmall.product.service.SpuSaleAttrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;


@RequestMapping("/api/inner/rpc/product")
@RestController
public class SkuDetailApiController {

    @Autowired
    SkuInfoService skuInfoService;

    @Autowired
    SpuSaleAttrService spuSaleAttrService;

    @Autowired
    BaseCategory3Service baseCategory3Service;
/*
    @GetMapping("/skudetail/{skuId}")
    public Result<SkuDetailTo> getDetail(@PathVariable("skuId") Long skuId){

//        准备查询所有需要的数据
       SkuDetailTo skuDetailTo = skuInfoService.getSkuDetail(skuId);
        return Result.ok(skuDetailTo);

    }

*/

    //   1. 查询skuinfo信息
    @GetMapping("/skudetail/info/{skuId}")
    public Result<SkuInfo> getSkuInfo(@PathVariable("skuId") Long skuId) {

        SkuInfo skuInfo = skuInfoService.getDetailSkuInfo(skuId);
        return Result.ok(skuInfo);

    }

    //    2.查询商品图片信息
    @GetMapping("/skudetail/images/{skuId}")
    public Result<List<SkuImage>> getSkuImages(@PathVariable("skuId") Long skuId) {
        List<SkuImage> imageList = skuInfoService.getDetailSkuImages(skuId);
        return Result.ok(imageList);
    }

//    3.获取商品的实时价格

    @GetMapping("/skudetail/price/{skuId}")
    public Result<BigDecimal> getSku1010Price(@PathVariable("skuId") Long skuId) {
        BigDecimal price = skuInfoService.get1010Price(skuId);
        return Result.ok(price);
    }

    //    4.查询销售属性名和值
    @GetMapping("/skudetail/saleattrvalues/{skuId}/{spuId}")
    public Result<List<SpuSaleAttr>> getSkuSalAttrValues(@PathVariable("skuId")
                                                                 Long skuId,
                                                         @PathVariable("spuId")
                                                                 Long spuId) {
        List<SpuSaleAttr> saleAttrList = spuSaleAttrService.
                getSaleAttrAndValueMarkSku(spuId, skuId);

        return Result.ok(saleAttrList);
    }

    //    5.查询sku组合 valueJson
    @GetMapping("/skudetail/valueJson/{spuId}")
    public Result<String> getSkuValueJson(
            @PathVariable("spuId")
                    Long spuId) {

        String valuejson = spuSaleAttrService.getAllSkuSaleAttrValueJson(spuId);

        return Result.ok(valuejson);
    }

//    6.根据三级分类id查询分类
    @GetMapping("/skudetail/categoryview/{c3Id}")
    public Result<CategoryViewTo> getCategoryView(@PathVariable("c3Id") Long c3Id){

        CategoryViewTo categoryViewTo= baseCategory3Service.getCategoryView(c3Id);


        return Result.ok(categoryViewTo);

    }
}