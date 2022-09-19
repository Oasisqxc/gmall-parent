package com.atguigu.gmall.order.service;

import com.atguigu.gmall.model.enums.ProcessStatus;
import com.atguigu.gmall.model.order.OrderInfo;
import com.atguigu.gmall.model.vo.order.OrderSubmitVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author hp
* @description 针对表【order_info(订单表 订单表)】的数据库操作Service
* @createDate 2022-09-17 00:45:00
*/
public interface OrderInfoService extends IService<OrderInfo> {

//    把订单信息保存到数据库
    Long saveOrder(OrderSubmitVo submitVo, String tradeNo);

//    关闭订单
    void changeOrderStatus(Long orderId, Long userId, ProcessStatus closed, List<ProcessStatus> expected);
}
