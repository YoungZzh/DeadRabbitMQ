package com.lianlianpay.deadrabbitmq.config;

import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * @description: 消息发送确认
 * @author: YoungZzh
 * @date: 2020/11/6 9:36
 * @version: v1.0
 */
@Configuration
public class RabbitTemplateConfig implements RabbitTemplate.ReturnCallback ,RabbitTemplate.ConfirmCallback{

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostConstruct
    public void init(){
        rabbitTemplate.setReturnCallback(this::returnedMessage);
        rabbitTemplate.setConfirmCallback(this::confirm);
    }
    /**
     * 通过实现ConfirmCallBack接口，消息发送到Broker后触发回调，确认消息是否
     * 到达Broker服务器，也就是只确认正确到达Exchange中
     * @param message 消息主体
     * @param i 消息回复码replyCode
     * @param s 消息回复描述replyText
     * @param s1    使用的交换机exchange
     * @param s2    使用的路由键routing
     */
    @Override
    public void returnedMessage(Message message, int i, String s, String s1, String s2) {

    }

    /**
     * 通过实现ReturnCallBack接口，启动消息失败返回，比如路由不到队列时触发回调
     * @param correlationData   消息唯一标识
     * @param b     确认结果ack
     * @param s     失败原因cause
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean b, String s) {

    }

    RabbitListenerContainerFactory<?> rabbitListenerContainerFactory(ConnectionFactory connectionFactory){
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(new Jackson2JsonMessageConverter());
        /**
         * 配置消息接受确认机制，有三种模式:
         *  1.AcknowledgeMode.NONE 自动确认ack模式，默认开启
         *  2.AcknowledgeMode.MANUAL 手动确认
         *  3.AcknowledgeMode.AUTO  根据情况确定
         * 如果手动确认则当消费者调用ack、nack、reject几种方法进行确认，手动确认可以在业务
         * 失败后进行一些操作，如消息未被ACK则会发送到下一个消费者
         *
         * 如果某个服务忘记ACK了，则RabbitMQ不会再发送数据给它，因为RabbitMQ认为服务的处理能力有限
         *
         * 也可以再yml文件当中开启此配置
         */
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        return factory;
    }
}
