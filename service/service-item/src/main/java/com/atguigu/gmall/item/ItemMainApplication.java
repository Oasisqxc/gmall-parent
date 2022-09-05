package com.atguigu.gmall.item;

import com.atguigu.gmall.common.annotation.EnableThreadPool;
import com.atguigu.gmall.common.config.RedissonAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;

/**
 * 1、公共的配置搬家放到 service-util
 * 2、当前项目依赖了 service-util
 *
 * 当前应用启动只会扫描 ItemMainApplication 所在包的所有组件
 * - com.atguigu.gmall.item.*****
 * - com.atguigu.gmall.common.**
 */
//@EnableAspectJAutoProxy //开启aspectj的自动代理功能
//@Import(RedissonAutoConfiguration.class)
@EnableThreadPool
@EnableFeignClients
@SpringCloudApplication
public class ItemMainApplication {

    public static void main(String[] args) {
        SpringApplication.run(ItemMainApplication.class,args);
    }
}
