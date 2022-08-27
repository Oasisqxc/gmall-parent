package com.atguigu.gmall.product.controller;


import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.SkuInfo;
import com.atguigu.gmall.product.service.SkuInfoService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "sku管理")
@RestController
@RequestMapping("/admin/product")
public class SkuController {

    @Autowired
    SkuInfoService skuInfoService;

//    admin/product/list/1/10
    @ApiOperation("sku分页查询")
    @GetMapping("/list/{pn}/{ps}")
    public Result skuList(@PathVariable("pn") Long pn,
                          @PathVariable("ps") Long ps
                          ){

        Page<SkuInfo> page = new Page<>(pn, ps);
        Page<SkuInfo> result = skuInfoService.page(page);
        return  Result.ok(result);
    }

//    /admin/product/saveSkuInfo
//    sku大保存
    @ApiOperation("sku所有属性保存")
    @PostMapping("/saveSkuInfo")
    public Result saveSku(@RequestBody SkuInfo info){

       skuInfoService.saveSkuInfo(info);

        return  Result.ok();

    }

//    商品上架，上架为1，下架为0
//    admin/product/onSale/11
    @ApiOperation("商品上架")
    @GetMapping("/onSale/{skuId}")
    public Result onSale(@PathVariable("skuId") Long skuId){

        skuInfoService.updateOnSale(skuId);

        return Result.ok();
    }

    @ApiOperation("商品下架")//下架为0
    @GetMapping("/cancelSale/{skuId}")
    public Result cancelSale(@PathVariable("skuId") Long skuId){

        skuInfoService.updateCancelSale(skuId);

        return Result.ok();
    }
}
