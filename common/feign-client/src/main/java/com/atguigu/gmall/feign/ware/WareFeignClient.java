package com.atguigu.gmall.feign.ware;

import com.atguigu.gmall.feign.ware.callback.WareFeignClientCallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "ware-manage",url = "${app.ware-url:http://localhost:9001/}"
,fallback = WareFeignClientCallback.class
)
public interface WareFeignClient {

//    查询一个商品是否有库存
    @GetMapping("/hasStock")
    String hasStock(@RequestParam("skuId") Long skuId,
                    @RequestParam("num") Integer num
                    );
}
