package com.atguigu.gmall.feign.cart;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.cart.CartInfo;
import com.atguigu.gmall.model.product.SkuInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequestMapping("/api/inner/rpc/cart")
@FeignClient("service-cart")
public interface CartFeignClient {
    /**
     * 把商品添加到购物车
     * @param skuId
     * @param num
     * @return  把那个商品添加到了购物车
     */
    @GetMapping("/addToCart")
    public Result<Object> addToCart(@RequestParam("skuId") Long skuId,
                                     @RequestParam("num") Integer num
    );

    //    删除购物车中商品 /cart/deleteChecked
    @GetMapping("/deleteChecked")
    public Result deleteChecked();

    //    获取当前购物车中选中的所有商品
    @GetMapping("/checked/list")
    public Result<List<CartInfo>> getChecked();
}
