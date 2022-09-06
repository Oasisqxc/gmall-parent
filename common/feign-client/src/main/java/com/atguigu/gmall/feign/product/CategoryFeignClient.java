package com.atguigu.gmall.feign.product;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.to.CategoryTreeTo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

//告诉SpringBoot 这是一个远程调用的客户端，调用 service-product 微服务的功能
//远程调用之前feign会自己找nacos要到 service-product 真的地址
@FeignClient("service-product")
@RequestMapping("/api/inner/rpc/product")
public interface CategoryFeignClient {
//1.给service-product 发送一个GET 方式的请求 路径是 "/api/inner/rpc/product/category/tree
//    2.拿到远程的响应 json 结果后转成 Result类型的对象，并且返回的数据是 list<CategoryTreeTo>
@GetMapping("/category/tree")
Result<List<CategoryTreeTo>> getAllCategoryWithTree();

}
