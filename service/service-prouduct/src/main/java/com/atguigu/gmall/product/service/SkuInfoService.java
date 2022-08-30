package com.atguigu.gmall.product.service;

import com.atguigu.gmall.model.product.SkuImage;
import com.atguigu.gmall.model.product.SkuInfo;
import com.atguigu.gmall.model.to.SkuDetailTo;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
* @author hp
* @description 针对表【sku_info(库存单元表)】的数据库操作Service
* @createDate 2022-08-24 10:58:22
*/
public interface SkuInfoService extends IService<SkuInfo> {

    void saveSkuInfo(SkuInfo info);

    void updateOnSale(Long skuId);

    void updateCancelSale(Long skuId);
//获取商品详情数据
    SkuDetailTo getSkuDetail(Long skuId);
//    获取sku的实时价格
    BigDecimal get1010Price(Long skuId);
//查询skuinfo信息
    SkuInfo getDetailSkuInfo(Long skuId);
// 获取sku的所有图片
    List<SkuImage> getDetailSkuImages(Long skuId);
}
