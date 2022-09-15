package com.atguigu.gmall.cart.controller;

import com.atguigu.gmall.cart.service.CartService;
import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.cart.CartInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartRestController {

    @Autowired
    CartService cartService;

    @GetMapping("/cartList")
    public Result cartList() {

//        1.先决定用哪个购物车键
        String cartKey = cartService.determinCartKey();

//        2.先尝试合并购物车
        cartService.mergeUserAndTempCart();
//        3.获取商品购物车中的所有商品
        List<CartInfo> infos = cartService.getCarList(cartKey);

        return Result.ok(infos);

    }

//    修改购物车数量

    //    api/cart/addToCart/50/1
    @PostMapping("/addToCart/{skuId}/{num}")
    public Result updateItemNum(@PathVariable("skuId") Long skuId
            , @PathVariable("num") Integer num
    ) {

        String cartKey = cartService.determinCartKey();
        cartService.updateItemNum(skuId, num, cartKey);
        return Result.ok();
    }

    //    更新购物车商品中的选中状态
//    /api/cart/checkCart/50/0
    @GetMapping("/checkCart/{skuId}/{status}")
    public Result check(
            @PathVariable("skuId") Long skuId,
            @PathVariable("status") Integer status
    ) {
        String cartKey = cartService.determinCartKey();
        cartService.updateChecked(skuId, status, cartKey);
        return Result.ok();
    }

//      删除购物车中选中商品
//api/cart/deleteCart/50
     @DeleteMapping("/deleteCart/{skuId}")
    public Result deleteCart(@PathVariable("skuId") Long skuId){
         String cartKey = cartService.determinCartKey();
         cartService.deleteCartItem(skuId,cartKey);
        return Result.ok();
     }



}
