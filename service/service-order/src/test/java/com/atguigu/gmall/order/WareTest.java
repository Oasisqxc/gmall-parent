package com.atguigu.gmall.order;

import com.atguigu.gmall.feign.ware.WareFeignClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class WareTest {

    @Autowired
    WareFeignClient wareFeignClient;
    @Test
    void testHasStock(){
        String hasStock = wareFeignClient.hasStock(43L, 2);
        System.out.println(hasStock);


        String hasStock2 = wareFeignClient.hasStock(490L, 2);
        System.out.println(hasStock2);
    }
}
