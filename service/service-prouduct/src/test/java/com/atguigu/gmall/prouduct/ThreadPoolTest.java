package com.atguigu.gmall.prouduct;

import com.atguigu.gmall.product.ProuductMainApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.UUID;
import java.util.concurrent.ThreadPoolExecutor;

@SpringBootTest(classes = ProuductMainApplication.class)
public class ThreadPoolTest {

    @Resource
    ThreadPoolExecutor executor;


    @Test
    void testPool2() throws InterruptedException {
        for (int i = 0; i <100 ; i++) {
            executor.submit(()->{
                System.out.println(
                        Thread.currentThread().getName() +
                                ":" + UUID.randomUUID().toString());


            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            });

        }

        Thread.sleep(10000L);
    }
}
