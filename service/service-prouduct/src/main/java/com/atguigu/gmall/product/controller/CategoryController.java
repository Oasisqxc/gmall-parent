package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.BaseCategory1;
import com.atguigu.gmall.model.product.BaseCategory2;
import com.atguigu.gmall.model.product.BaseCategory3;
import com.atguigu.gmall.product.service.BaseCategory1Service;
import com.atguigu.gmall.product.service.BaseCategory2Service;
import com.atguigu.gmall.product.service.BaseCategory3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

//@ResponseBody   所有的响应数据都直接写给浏览器（如果是对象写成json，如果是文本就写成普通字符串）
//@Controller 这个类是来接受请求的
@RestController
@RequestMapping("/admin/product")
public class CategoryController {

    @Autowired
    BaseCategory1Service baseCategory1Service;
    @Autowired
    BaseCategory2Service baseCategory2Service;

    @Autowired
    BaseCategory3Service baseCategory3Service;
//  admin/product/getCategory1
// {code:2000,message:"ok",data: [{id:1},{id:2},{id:3}]}
//利用MyBatisPlus提供好的CRUD方法，直接查出所有一级分类
    @GetMapping("/getCategory1")
 public Result getCategory1(){
     List<BaseCategory1> list = baseCategory1Service.list();
     return Result.ok(list);
 }

// http://192.168.200.1/admin/product/getCategory2/2

    @GetMapping("/getCategory2/{c1Id}")
    public Result getCategory2(@PathVariable("c1Id") Long c1Id){
//     查询出 父分类id是c1Id的所有二级分类
      List<BaseCategory2> category2s =   baseCategory2Service.getCategory1Child(c1Id);
        return Result.ok(category2s);
    }

//    获取三级分类
    @GetMapping("/getCategory3/{c2Id}")
    public Result getCategor3(@PathVariable("c2Id") Long c2Id){
//        查询出 父分类id是C2Id的所有三级分类

       List<BaseCategory3> category3s= baseCategory3Service.getCategory2Child(c2Id);

        return Result.ok(category3s);

    }
}
