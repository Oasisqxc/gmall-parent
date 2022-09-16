package com.atguigu.gmall.web;

import com.atguigu.gmall.common.annotation.EnableAutoFeignInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableAutoFeignInterceptor
@EnableFeignClients(basePackages = {"com.atguigu.gmall.feign.item",
"com.atguigu.gmall.feign.product",
        "com.atguigu.gmall.feign.search",
        "com.atguigu.gmall.feign.cart"
})
@SpringCloudApplication
public class WebAllMainApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebAllMainApplication.class,args);
    }
}
