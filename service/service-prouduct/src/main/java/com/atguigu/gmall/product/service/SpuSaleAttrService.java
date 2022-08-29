package com.atguigu.gmall.product.service;

import com.atguigu.gmall.model.product.SpuSaleAttr;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;


/**
 * @author hp
 * @description 针对表【spu_sale_attr(spu销售属性)】的数据库操作Service
 * @createDate 2022-08-24 10:58:21
 */
public interface SpuSaleAttrService extends IService<SpuSaleAttr> {
    List<SpuSaleAttr> getSaleAttrAndValueBySpuId(Long spuId);

    List<SpuSaleAttr> getSaleAttrAndValueMarkSku(Long spuId, Long skuId);

    String getAllSkuSaleAttrValueJson(Long spuId);


//根据spuId查询对应的所有销售属性名和值



}


