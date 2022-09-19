package com.atguigu.gmall.order;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RabbitTest {
    @Autowired
    RabbitTemplate rabbitTemplate;
    @Test
    void test1(){

        rabbitTemplate.convertAndSend("hahax","haa","123");

    }

}

