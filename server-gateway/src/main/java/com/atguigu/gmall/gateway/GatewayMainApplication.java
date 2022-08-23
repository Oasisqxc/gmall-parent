package com.atguigu.gmall.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

//@EnableCircuitBreaker  //开启服务熔断降级、流量保护 [1、导入jar  2、使用这个注册]
//@EnableDiscoveryClient //开启服务发现[1、导入服务发现jar  2、使用这个注解]
//@SpringBootApplication
@SpringCloudApplication//这个是上面三合一版本
public class GatewayMainApplication {
    public static void main(String[] args) {
        SpringApplication.run(GatewayMainApplication.class,args);
    }

}
