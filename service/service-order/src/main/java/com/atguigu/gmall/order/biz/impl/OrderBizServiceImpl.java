package com.atguigu.gmall.order.biz.impl;

import com.atguigu.gmall.common.auth.AuthUtils;
import com.atguigu.gmall.common.constant.SysRedisConst;
import com.atguigu.gmall.common.execption.GmallException;
import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.common.result.ResultCodeEnum;
import com.atguigu.gmall.feign.cart.CartFeignClient;
import com.atguigu.gmall.feign.product.SkuProductFeignClient;
import com.atguigu.gmall.feign.user.UserFeignClient;
import com.atguigu.gmall.feign.ware.WareFeignClient;
import com.atguigu.gmall.model.cart.CartInfo;
import com.atguigu.gmall.model.enums.ProcessStatus;
import com.atguigu.gmall.model.user.UserAddress;
import com.atguigu.gmall.model.vo.order.CartInfoVo;
import com.atguigu.gmall.model.vo.order.OrderConfirmDataVo;
import com.atguigu.gmall.model.vo.order.OrderSubmitVo;
import com.atguigu.gmall.model.vo.user.UserAuthInfo;
import com.atguigu.gmall.order.biz.OrderBizService;
import com.atguigu.gmall.order.service.OrderInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class OrderBizServiceImpl implements OrderBizService {

    @Autowired
    CartFeignClient cartFeignClient;

    @Autowired
    UserFeignClient userFeignClient;

    @Autowired
    SkuProductFeignClient skuProductFeignClient;

    @Autowired
    WareFeignClient wareFeignClient;

    @Autowired
    StringRedisTemplate redisTemplate;

    @Autowired
    OrderInfoService orderInfoService;

    @Override
    public OrderConfirmDataVo getConfirmData() {
        OrderConfirmDataVo vo = new OrderConfirmDataVo();
//1.?????????????????????????????????
        List<CartInfo> data = cartFeignClient.getChecked().getData();

        List<CartInfoVo> infoVos = data.stream()
                .map(cartInfo -> {
                    CartInfoVo infoVo = new CartInfoVo();
                    infoVo.setSkuId(cartInfo.getSkuId());
                    infoVo.setImgUrl(cartInfo.getImgUrl());
                    infoVo.setSkuName(cartInfo.getSkuName());

//                    ???????????????????????????????????????????????????????????????????????????????????????????????????????????????
                    //                    ????????????

                    Result<BigDecimal> price = skuProductFeignClient
                            .getSku1010Price(cartInfo.getSkuId());
                    infoVo.setOrderPrice(price.getData());
                    infoVo.setSkuNum(cartInfo.getSkuNum());

//                    ??????????????????
//                    feign ????????? HTTP?????????

                    String stock
                            = wareFeignClient.hasStock(cartInfo.getSkuId(), cartInfo.getSkuNum());
                    infoVo.setHasStock(stock);

                    return infoVo;

                }).collect(Collectors.toList());
        vo.setDetailArrayList(infoVos);
        //2???????????????????????????
        Integer totalNum = infoVos.stream().map(CartInfoVo::getSkuNum)
                .reduce((o1, o2) -> o1 + o2)
                .get();
        vo.setTotalNum(totalNum);

        //3???????????????????????????
        BigDecimal totalAmount = infoVos.stream()
                .map(item -> item.getOrderPrice().multiply(new BigDecimal(item.getSkuNum() + "")))
                .reduce((o1, o2) -> o1.add(o2)).get();
        vo.setTotalAmount(totalAmount);

//        4.??????????????????????????????
        Result<List<UserAddress>> addressList = userFeignClient.getUserAddressList();
        vo.setUserAddressList(addressList.getData());

//        5.?????????????????????
//        String tradeNo = UUID.randomUUID().toString().replace("_", "");
//        vo.setTradeNo(tradeNo);
//        ???????????????????????????????????????
        String tradeNo =generateTradeNo();
//        ?????????????????????
        vo.setTradeNo(tradeNo);
        return vo;
    }

    @Override
    public String generateTradeNo() {

        long millis = System.currentTimeMillis();
        UserAuthInfo info = AuthUtils.getCurrentAuthInfo();
        String tradeNo = millis+"_"+info.getUserId();
//        ??????redis?????????
//        ??????????????????????????????????????????
        redisTemplate.opsForValue()
                .set(SysRedisConst.ORDER_TEMP_TOKEN+tradeNo,"1",15, TimeUnit.MINUTES);
        return tradeNo;
    }

    @Override
    public boolean checkTradeNo(String tradeNo) {

        //1????????????????????????????????????????????????, 1, 0 ?????????????????????
        String lua = "if redis.call(\"get\",KEYS[1]) == ARGV[1] then " +
                "    return redis.call(\"del\",KEYS[1]) " +
                "else " +
                "    return 0 " +
                "end";

        Long execute = redisTemplate.execute(new DefaultRedisScript<Long>(lua, Long.class),
                Arrays.asList(SysRedisConst.ORDER_TEMP_TOKEN + tradeNo),
                new String[]{"1"});
        if (execute >0){
//            ?????????????????????????????????
            return  true;
        }

        return false;


    }

//    ????????????
    @Override
    public Long submitOrder(OrderSubmitVo submitVo, String tradeNo) {
//1. ?????????
        boolean checkTradeNo = checkTradeNo(tradeNo);
        if (!checkTradeNo){
            throw new GmallException(ResultCodeEnum.TOKEN_INVAILD);
        }

//        2.?????????
        List<String> noStockSkus = new ArrayList<>();
        for (CartInfoVo infoVo : submitVo.getOrderDetailList()) {
            Long skuId = infoVo.getSkuId();
            String stock = wareFeignClient.hasStock(skuId, infoVo.getSkuNum());
            if(!"1".equals(stock)){
                noStockSkus.add(infoVo.getSkuName());
            }
        }

        if (noStockSkus.size() >0){
            GmallException exception = new GmallException(ResultCodeEnum.ORDER_NO_STOCK);
            String skuNames = noStockSkus.stream()
                    .reduce((s1, s2) -> s1 + " " + s2
                    ).get();
            throw new GmallException(
                    ResultCodeEnum.ORDER_NO_STOCK.getMessage() + skuNames,
                    ResultCodeEnum.ORDER_NO_STOCK.getCode());


        }

//        3????????????
        List<String> skuNames = new ArrayList<>();
        for (CartInfoVo infoVo : submitVo.getOrderDetailList()) {

            Result<BigDecimal> price = skuProductFeignClient.getSku1010Price(infoVo.getSkuId());


            if (!price.getData().equals(infoVo.getOrderPrice())){
                skuNames.add(infoVo.getSkuName());
            }

        }

        if (skuNames.size()>0){
            String skuName = skuNames.stream()
                    .reduce((s1, s2) -> s1 + " " + s2)

                    .get();
//            ??????????????????????????????
            throw  new GmallException(
                    ResultCodeEnum.ORDER_PRICE_CHANGED.getMessage() + "<br/>" +skuName,
                    ResultCodeEnum.ORDER_PRICE_CHANGED.getCode());

        }
        //4????????????????????????????????????
       Long orderId =   orderInfoService.saveOrder(submitVo,tradeNo);

//        5.??????????????????????????????
        cartFeignClient.deleteChecked();



        return orderId;
    }

//    ????????????
    @Override
    public void closeOrder(Long orderId, Long userId) {
        ProcessStatus closed = ProcessStatus.CLOSED;
// ??????????????????????????????????????????????????????
        List<ProcessStatus> expected
                = Arrays.asList(ProcessStatus.UNPAID, ProcessStatus.FINISHED);

        orderInfoService.changeOrderStatus(orderId,userId,closed,expected);

    }


}
