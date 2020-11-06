package com.lianlianpay.deadrabbitmq.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @description: TODO
 * @author: YoungZzh
 * @date: 2020/11/5 15:36
 * @version: v1.0
 */
@Configuration
public class RabbitConfig {
    @Value("${sunspring.order.exchange}")
    public String orderExchange;

    @Value("${sunspring.order.queue}")
    public String orderQueue;

    @Value("${sunspring.order.routingKey}")
    public String orderRoutingKey;

    @Value("${sunspring.dlx.exchange}")
    private String dlxExchange;

    @Value("${sunspring.dlx.queue}")
    private String dlxQueue;

    @Value("${sunspring.dlx.routingKey}")
    private String dlxRoutingKey;

    /**
     * 声明死信交换机
     * @return
     */
    @Bean
    public DirectExchange dlxExchange(){
        return new DirectExchange(dlxExchange);
    }

    /**
     * 声明死信队列
     * @return
     */
    @Bean
    public Queue dlxQueue(){
        return new Queue(dlxQueue);
    }

    /**
     * 绑定死信队列到死信交换机
     * @return
     */
    @Bean
    public Binding binding(){
        return BindingBuilder.bind(dlxQueue())
                .to(dlxExchange())
                .with(dlxRoutingKey);
    }

    /**
     * 声明订单业务交换机
     * @return
     */
    @Bean
    public DirectExchange orderExchange(){
        return new DirectExchange(orderExchange);
    }

    /**
     * 什么订单业务队列
     * @return
     */
    @Bean
    public Queue orderQueue(){
        Map<String,Object> arguments = new HashMap<>();
        //绑定该队列到死信交换机
        arguments.put("x-dead-letter-exchange",dlxExchange);
        arguments.put("x-dead-letter-routing-key",dlxRoutingKey);
        return new Queue(orderQueue,true,false,false,arguments);
    }

    /**
     * 绑定订单队列到订单交换机
     * @return
     */
    @Bean
    public Binding orderBinding(){
        return BindingBuilder.bind(orderQueue())
                .to(orderExchange())
                .with(orderRoutingKey);
    }

}
