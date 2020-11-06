package com.lianlianpay.deadrabbitmq.consumer;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @description: TODO
 * @author: YoungZzh
 * @date: 2020/11/5 16:45
 * @version: v1.0
 */
@Service
public class ConService {

    @RabbitListener(queues = "sunspring.dlx.queue")
    public void dlxListener(Message message, Channel channel) throws IOException {
        System.out.println(new String(message.getBody()));

        //对消息进行业务处理....
        channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
    }
}
