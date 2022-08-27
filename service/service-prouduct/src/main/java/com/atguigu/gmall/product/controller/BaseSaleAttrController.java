package com.atguigu.gmall.product.controller;


import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.BaseSaleAttr;
import com.atguigu.gmall.model.product.SpuSaleAttr;
import com.atguigu.gmall.product.service.BaseSaleAttrService;
import com.atguigu.gmall.product.service.SpuSaleAttrService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "获取全平台的销售属性")
@RestController
@RequestMapping("/admin/product")
public class BaseSaleAttrController {

    @Autowired
    BaseSaleAttrService  baseSaleAttrService;

    @Autowired
    SpuSaleAttrService spuSaleAttrService;

//    /baseTrademark/getTrademarkList
    @ApiOperation("获取所有销售属性的名字")
    @GetMapping("/baseSaleAttrList")
    public Result baseSaleAttrList(){
        //操作base_sale_attr表
        List<BaseSaleAttr> list = baseSaleAttrService.list();
        return Result.ok(list);
    }

//    admin/product/spuSaleAttrList/{spuId}
//    查询出指定spu当时定义的所有销售属性的名和值
    @ApiOperation("查询出指定spu当时定义的所有销售属性的名和值")
    @GetMapping("/spuSaleAttrList/{spuId}")
    public Result spuSaleAttrList(@PathVariable("spuId") Long spuId){

     List<SpuSaleAttr> spuSaleAttrs= spuSaleAttrService.getSaleAttrAndValueBySpuId(spuId);

        return Result.ok(spuSaleAttrs);
    }


}
