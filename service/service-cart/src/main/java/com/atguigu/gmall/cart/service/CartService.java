package com.atguigu.gmall.cart.service;

import com.atguigu.gmall.model.cart.CartInfo;
import com.atguigu.gmall.model.product.SkuInfo;

import java.util.List;

public interface CartService {
//    添加一个商品到购物车
    SkuInfo addToCart(Long skuId,Integer num);

//    根据用户登录决定使用哪个购物车
    String determinCartKey();

//指定商品添加到购物车
SkuInfo addItemToCart(Long skuId, Integer num, String cartKey);

//从购物车中获取某个商品
   CartInfo getItemFromCart(String cartKey,Long skuId);

//   获取购物车中的所有商品
    List<CartInfo> getCarList(String cartKey);

//    修改购物车数量
    void updateItemNum(Long skuId, Integer num, String cartKey);

//    更新购物车所选商品状态
    void updateChecked(Long skuId, Integer status, String cartKey);

//    删除购物车商品
    void deleteCartItem(Long skuId, String cartKey);

//    删除购物车中商品
    void deleteChecked(String cartKey);

//    获取购物车中选中的商品
List<CartInfo>   getCheckedItems(String cartKey);

// 合并购物车
    void mergeUserAndTempCart();

//    更新购物车中所有商品的价格
   void updateCartAllItemsPrice(String cartKey, List<CartInfo> cartInfos);
}
