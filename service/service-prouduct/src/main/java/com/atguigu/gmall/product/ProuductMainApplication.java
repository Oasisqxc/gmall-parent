package com.atguigu.gmall.product;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
//自动扫描这个包下的所有Mapper接口
@MapperScan(basePackages = "com.atguigu.gmall.product.mapper")
@SpringCloudApplication
public class ProuductMainApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProuductMainApplication.class,args);
    }

}
