package com.atguigu.gmall.item.cache;

import com.atguigu.gmall.model.to.SkuDetailTo;

public interface CacheOpsService {
    <T>T getCacheData(String cacheKey, Class<T> clz);

    boolean bloomContains(Long skuId);

    boolean tryLock(Long skuId);
//把指定对象使用指定的key保存到redis
    void saveData(String cacheKey, Object fromRpc);

    void unlock(Long skuId);
}
