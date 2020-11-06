package com.lianlianpay.deadrabbitmq.controller;

import com.alibaba.fastjson.JSON;
import com.lianlianpay.deadrabbitmq.config.RabbitConfig;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @description: TODO
 * @author: YoungZzh
 * @date: 2020/11/5 16:31
 * @version: v1.0
 */
@RestController
public class PubController {
    @Resource
    private RabbitTemplate rabbitTemplate;

    @Resource
    private RabbitConfig rabbitConfig;

    public void sendDlx(){
        Object object = new Object();
        rabbitTemplate.convertAndSend(rabbitConfig.orderExchange,rabbitConfig.orderRoutingKey, JSON.toJSON(object),message -> {
            message.getMessageProperties().setDeliveryMode(MessageDeliveryMode.NON_PERSISTENT);
            message.getMessageProperties().setExpiration("10000");
            return message;
        });
    }
}
