package com.shown.rabbitmq.eight;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.impl.AMQBasicProperties;
import com.shown.rabbitmq.utils.RabbitmqInstance;

public class Producer {
    public static final String NORMAL_EXCHANGE = "normal_exchange";

    public static void main(String[] args) throws Exception{
        Channel channel = RabbitmqInstance.getChannel();
        //设置TTL 过期时间  单位是ms 10000ms = 10s
        AMQP.BasicProperties properties =
                new AMQP.BasicProperties().builder().expiration("10000").build();
        for (int i = 1; i < 11 ;i++){
            String message = "info" + i;
            channel.basicPublish(NORMAL_EXCHANGE,"zhangsan",properties,message.getBytes());
        }

    }
}
