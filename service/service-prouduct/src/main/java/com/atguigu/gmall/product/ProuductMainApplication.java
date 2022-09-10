package com.atguigu.gmall.product;

import com.atguigu.gmall.common.annotation.EnableThreadPool;
import com.atguigu.gmall.common.config.Swagger2Config;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

//自动扫描这个包下的所有Mapper接口
@EnableFeignClients(basePackages = "com.atguigu.gmall.feign.search")
@EnableScheduling
@EnableThreadPool
//@Import({Swagger2Config.class, RedissonAutoConfiguration.class})
@Import({Swagger2Config.class})
@MapperScan(basePackages = "com.atguigu.gmall.product.mapper")
@SpringCloudApplication
public class ProuductMainApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProuductMainApplication.class,args);
    }

}
