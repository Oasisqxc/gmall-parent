package com.atguigu.gmall.model.to;

import com.atguigu.gmall.model.product.BaseCategoryView;
import com.atguigu.gmall.model.product.SkuInfo;
import com.atguigu.gmall.model.product.SpuSaleAttr;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class SkuDetailTo {

//1.当前sku所属分类信息
    private CategoryViewTo categoryView;

//    2.商品的基本信息
    private SkuInfo skuInfo;

//    3.商品实时价格
    private BigDecimal price;

//    4.商品平台销售属性
    private List<SpuSaleAttr> spuSaleAttrList;

//    5.valuesSkuJson
    private String valuesSkuJson;


}
