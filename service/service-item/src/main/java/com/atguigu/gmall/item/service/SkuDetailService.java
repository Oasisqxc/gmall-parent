package com.atguigu.gmall.item.service;

import com.atguigu.gmall.model.to.SkuDetailTo;

public interface SkuDetailService {

    SkuDetailTo getSkuDetail(Long skuId);

//    更新热度分
    void updateHotScore(Long skuId);
}
