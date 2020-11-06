package com.lianlianpay.deadrabbitmq.consumer;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

/**
 * @description: 消息接受确认
 * @author: YoungZzh
 * @date: 2020/11/6 10:50
 * @version: v1.0
 */
@Service
@RabbitListener(queues = "sunspring_order_exchange")
public class AckConsumer {

    /**拒绝消息*/
    @RabbitHandler
    public void processMessage1(String message, Channel channel, @Headers Map<String,Object> map){
        if (map.get("error") != null){
            try {
                /**
                 * basicNack又两个参数：
                 *  deliveryTag：消息的唯一标识ID
                 *  multiple：为了减少网络流量，false只能处理单条，true为批量处理
                 *  Requeue: 是否会重入队列，重新发送，为true则会重复发送
                 *
                 *  basicNack和reject的区别在于，reject只能拒绝一条信息，并且不能重入队列，而basicNack比它强大
                 */
                channel.basicNack((Long)map.get(AmqpHeaders.DELIVERY_TAG),false,true);
                return;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**确认消息*/
    @RabbitHandler
    public void processMessage2(String message,Channel channel,@Headers Long tag){
        System.out.println(message);
        try {
            channel.basicAck(tag,false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
