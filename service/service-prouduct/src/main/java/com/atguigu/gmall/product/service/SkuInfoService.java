package com.atguigu.gmall.product.service;

import com.atguigu.gmall.model.product.SkuInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

/**
* @author hp
* @description 针对表【sku_info(库存单元表)】的数据库操作Service
* @createDate 2022-08-24 10:58:22
*/
public interface SkuInfoService extends IService<SkuInfo> {

    void saveSkuInfo(SkuInfo info);

    void updateOnSale(Long skuId);

    void updateCancelSale(Long skuId);
}
