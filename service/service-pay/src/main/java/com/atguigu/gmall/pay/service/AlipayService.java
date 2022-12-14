package com.atguigu.gmall.pay.service;

import com.alipay.api.AlipayApiException;

import java.util.Map;

public interface AlipayService {
    /**
     * 生成指定订单的支付页
     * @param orderId
     * @return
     */
    String getAlipayPageHtml(Long orderId) throws AlipayApiException;

    /**
     * 支付宝验签
     * @param paramMaps
     * @return
     */
    boolean rsaCheckV1(Map<String, String> paramMaps) throws AlipayApiException;
}
