package com.atguigu.gmall.feign.ware.callback;

import com.atguigu.gmall.feign.ware.WareFeignClient;
import org.springframework.stereotype.Component;

@Component
public class WareFeignClientCallback implements WareFeignClient {

    @Override
    public String hasStock(Long skuId, Integer num) {
//错误兜底
//        同意显示有货
        return "1";
    }
}
