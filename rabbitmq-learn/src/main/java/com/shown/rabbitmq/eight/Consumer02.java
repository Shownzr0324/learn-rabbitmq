package com.shown.rabbitmq.eight;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.shown.rabbitmq.utils.RabbitmqInstance;

import java.util.HashMap;
import java.util.Map;

/**
 * @author shown
 * 死信队列 消费死信队列
 */

public class Consumer02 {

    //死信队列的名称
    public static final String DEAD_QUEUE = "dead_queue";

    public static void main(String[] args) throws Exception{

        Channel channel = RabbitmqInstance.getChannel();


        System.out.println("Consumer02等待接收消息.....");
        DeliverCallback deliverCallback = (consumerTag,message) -> {
            System.out.println(new String(message.getBody()));
        };

        channel.basicConsume(DEAD_QUEUE,true,deliverCallback,consumerTag ->{});
    }


}
