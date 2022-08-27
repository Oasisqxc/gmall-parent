package com.atguigu.gmall.product.mapper;

import com.atguigu.gmall.model.product.SkuInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sun.org.apache.xml.internal.security.Init;
import org.apache.ibatis.annotations.Param;

/**
* @author hp
* @description 针对表【sku_info(库存单元表)】的数据库操作Mapper
* @createDate 2022-08-24 10:58:22
* @Entity com.atguigu.gmall.product.domain.SkuInfo
*/
public interface SkuInfoMapper extends BaseMapper<SkuInfo> {

    void updateIsSale(@Param("skuId") Long skuId,
                      @Param("sale") int sale
                      );


}




