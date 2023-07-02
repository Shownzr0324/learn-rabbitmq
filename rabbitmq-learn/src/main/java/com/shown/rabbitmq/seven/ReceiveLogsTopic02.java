package com.shown.rabbitmq.seven;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.shown.rabbitmq.utils.RabbitmqInstance;

public class ReceiveLogsTopic02 {
    public static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] args) throws Exception{

        //接收消息
        Channel channel = RabbitmqInstance.getChannel();

        channel.exchangeDeclare(EXCHANGE_NAME,"topic");

        String queueName = "Q2";
        channel.queueDeclare(queueName,false,false,false,null);
        channel.queueBind(queueName,EXCHANGE_NAME,"*.*.rabbit");
        channel.queueBind(queueName,EXCHANGE_NAME,"lazy.#");
        System.out.println("等待接收消息.....");


        DeliverCallback deliverCallback = (consumerTag,message) -> {
            System.out.println(new String(message.getBody()));
            System.out.println("接收队列:" + queueName + " 绑定键" + message.getEnvelope().getRoutingKey());
        };

        CancelCallback cancelCallback = consumerTag -> {
            System.out.println("消费者取消消费消息");
        };
        channel.basicConsume(queueName,true,deliverCallback,cancelCallback);


    }
}
