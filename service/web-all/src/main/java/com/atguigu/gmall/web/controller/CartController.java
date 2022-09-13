package com.atguigu.gmall.web.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.feign.cart.CartFeignClient;
import com.atguigu.gmall.model.product.SkuInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CartController {

    @Autowired
    CartFeignClient cartFeignClient;

    //添加商品到购物车
///cart.gmall.com/addCart.html?skuId=49&skuNum=1&sourceType=query
    @GetMapping("/addCart.html")
    public String addCarthtml(@RequestParam("skuId") Long skuId,
                              @RequestParam("skuNum") Integer skuNum,
                              Model model
    ) {
//1、把指定商品添加到购物车
        System.out.println("web-all 获取到的用户id：");
        Result<SkuInfo> result = cartFeignClient.addToCart(skuId, skuNum);
        model.addAttribute("skuInfo", result.getData());
        model.addAttribute("skuNum", skuNum);

        return "cart/addCart";
    }
}
