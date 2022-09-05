package com.atguigu.gmall.product.bloom;
/**
 * 重建指定布隆过滤器
 *
 */
public interface BloomOpsService {



    void rebuildBloom(String bloomSkuid, BloomDataQueryService bloomDataQueryService);
}
