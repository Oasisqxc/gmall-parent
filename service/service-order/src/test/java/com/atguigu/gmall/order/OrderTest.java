package com.atguigu.gmall.order;

import com.atguigu.gmall.model.order.OrderInfo;
import com.atguigu.gmall.order.mapper.OrderInfoMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
public class OrderTest {

    @Resource
    OrderInfoMapper orderInfoMapper;

    @Test
    void test1(){
        OrderInfo orderInfo = orderInfoMapper.selectById(205);
        System.out.println("orderInfo = " + orderInfo);

    }
}
