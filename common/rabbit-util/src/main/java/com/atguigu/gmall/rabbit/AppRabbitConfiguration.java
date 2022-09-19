package com.atguigu.gmall.rabbit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.amqp.RabbitTemplateConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.support.RetryTemplate;

@EnableRabbit
@Configuration
@Slf4j
public class AppRabbitConfiguration {

    @Bean
    RabbitTemplate rabbitTemplate(RabbitTemplateConfigurer configurer,
                                  ConnectionFactory connectionFactory
    ) {

        RabbitTemplate template = new RabbitTemplate();
        configurer.configure(template, connectionFactory);

//        感知消息是否真的被投递到队列[路由键错误、队列满了、消息没有持久化到磁盘...]
        template.setReturnCallback((Message message,
                                    int replyCode,
                                    String replyText,
                                    String exchange,
                                    String routingKey) -> {
//            消息没有被正确投递到队列
            log.error("消息投递队列失败，保存到数据库,{}", message);

        });

//        感知消息是否真的被投递到服务器[服务器连接有问题，错误的exchange...]
        template.setConfirmCallback(((CorrelationData correlationData,
                                      boolean ack,
                                      String cause) -> {
            if (!ack) {
                log.error("消息投递到服务器失败，保存到数据库，消息: {}", correlationData);
            }
        }));


//        设置重试器，发送失败会重试3次
        template.setRetryTemplate(new RetryTemplate());

        return template;
    }


}
