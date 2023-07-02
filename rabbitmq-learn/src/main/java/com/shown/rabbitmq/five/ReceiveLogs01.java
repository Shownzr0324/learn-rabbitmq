package com.shown.rabbitmq.five;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.shown.rabbitmq.utils.RabbitmqInstance;

public class ReceiveLogs01 {

    public static final String EXCHANGE_NAME = "shown";

    public static void main(String[] args) throws Exception{
        Channel channel = RabbitmqInstance.getChannel();

        //声明一个交换机
        channel.exchangeDeclare(EXCHANGE_NAME,"fanout");

        //1、声明一个队列
        String queueName = channel.queueDeclare().getQueue();

        //2、绑定交换机与队列
        channel.queueBind(queueName,EXCHANGE_NAME,"");
        System.out.println("等待接收消息，把接收到的消息打印在屏幕上......");

        //接收消息
        DeliverCallback deliverCallback = (messageTag,message) -> {
            System.out.println("ReceiveLogs01控制台打印接收到的消息：" + new String(message.getBody()));
        };

        //3、消费者消费消息
        channel.basicConsume(queueName,deliverCallback,messageTag -> {});
    }

}
