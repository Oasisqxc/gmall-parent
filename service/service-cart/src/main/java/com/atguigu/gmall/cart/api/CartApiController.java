package com.atguigu.gmall.cart.api;

import com.atguigu.gmall.common.constant.SysRedisConst;
import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.SkuInfo;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inner/rpc/cart")
public class CartApiController {


    /**
     * 把商品添加到购物车
     * @param skuId
     * @param num
     * @return  把那个商品添加到了购物车
     */
    @GetMapping("/addCart")
    public Result<SkuInfo> addToCart(@RequestParam("skuId") Long skuId,
                                     @RequestParam("num") Integer num,
                                     @RequestHeader(value = SysRedisConst.USERID_HEADER,required = false)
                                     String userId

                                     ){

        System.out.println("service-cart 获取到的用户id："+userId);

        return Result.ok();
    }
}
