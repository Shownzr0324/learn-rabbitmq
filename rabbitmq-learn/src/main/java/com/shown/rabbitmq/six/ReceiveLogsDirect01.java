package com.shown.rabbitmq.six;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.shown.rabbitmq.utils.RabbitmqInstance;

public class ReceiveLogsDirect01 {

    public static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] args) throws Exception{

        Channel channel = RabbitmqInstance.getChannel();

        //声明一个交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

        //1、声明一个队列
        channel.queueDeclare("console",false,false,false,null);

        //2、绑定交换机与队列
        /**
         * 1.队列名称
         * 2.交换机名称
         * 3.routingKey
         */
        channel.queueBind("console",EXCHANGE_NAME,"info");


        //接收消息
        DeliverCallback deliverCallback = (messageTag, message) -> {
            System.out.println("ReceiveLogsDirect01控制台打印接收到的消息：" + new String(message.getBody()));
        }; 

        //3、消费者消费消息
        channel.basicConsume("console",true,deliverCallback,messageTag -> {});
    }
}
