package com.atguigu.gmall.cart.api;

import com.atguigu.gmall.cart.service.CartService;
import com.atguigu.gmall.common.auth.AuthUtils;
import com.atguigu.gmall.common.constant.SysRedisConst;
import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.SkuInfo;
import com.atguigu.gmall.model.vo.search.user.UserAuthInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
@Slf4j
@RestController
@RequestMapping("/api/inner/rpc/cart")
public class CartApiController {

@Autowired
    CartService cartService;


    /**
     * 把商品添加到购物车
     * @param skuId
     * @param num
     * @return  把那个商品添加到了购物车
     */
    @GetMapping("/addToCart")
    public Result<SkuInfo> addToCart(@RequestParam("skuId") Long skuId,
                                     @RequestParam("num") Integer num
                                     ){

//        System.out.println("service-cart 获取到的用户id："+userId);
//
//        UserAuthInfo currentAuthInfo = AuthUtils.getCurrentAuthInfo();
//
//        log.info("用户id:{},临时id:{}",currentAuthInfo.getUserId(),currentAuthInfo.getUserTempId());

        SkuInfo skuInfo = cartService.addToCart(skuId, num);
        return Result.ok(skuInfo);
    }

//    删除购物车中商品 /cart/deleteChecked
    @GetMapping("/deleteChecked")
    public Result deleteChecked(){

        String cartKey = cartService.determinCartKey();
        cartService.deleteChecked(cartKey);
        return Result.ok();
    }
}
