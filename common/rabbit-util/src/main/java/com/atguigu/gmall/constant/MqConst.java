package com.atguigu.gmall.constant;

public class MqConst {

//    订单交换机名
    public static final String EXCHANGE_ORDER_EVNT = "order-event-exchange";
//    订单延迟队列名
    public static final String QUEUE_ORDER_DELAY = "order-delay-queue";
//     订单死信路由键
    public static final String RK_ORDER_DEAD = "order.dead";

//    死单队列名
    public static final String QUEUE_ORDER_DEAD = "order-dead-queue";
//    订单新建路由键
    public static final String RK_ORDER_CREATED = "order.created";
}
