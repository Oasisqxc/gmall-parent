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
        Result<Object> result = cartFeignClient.addToCart(skuId, skuNum);
        if (result.isOk()) {

            model.addAttribute("skuInfo", result.getData());
            model.addAttribute("skuNum", skuNum);

            return "cart/addCart";
        } else {
            String message = result.getMessage();
            model.addAttribute("msg", result.getData());
            return "cart/error";
        }

    }

    //    2.购物车列表页 http://cart.gmall.com/cart.html
    @GetMapping("/cart.html")
    public String cartHtml() {

        return "cart/index";
    }

    //    3.删除选中的购物车商品 /cart/deleteChecked
    @GetMapping("/cart/deleteChecked")
    public String deleteChecked() {

        cartFeignClient.deleteChecked();
//        redirect 重定向
        return "redirect:http://cart.gmall.com/cart.html";
    }
}
