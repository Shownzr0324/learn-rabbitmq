package com.shown.rabbitmq.two;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.shown.rabbitmq.utils.RabbitmqInstance;

/**
 * @author shown
 * @Description: 工作线程 相当于之前的消费者类
 */
public class Work01 {
    public static final String QUEUE_NAME = "Hello";

    public static void main(String[] args) throws Exception{
        Channel channel = RabbitmqInstance.getChannel();

        //创建DeliverCallback回调
        DeliverCallback deliverCallback = (consumerTag,message) -> {
            System.out.println("接收到的消息：" + new String(message.getBody()));
        };

        //创建CancelCallback回调
        CancelCallback cancelCallback = consumerTag -> {
            System.out.println(consumerTag + "消息者取消消费者回调逻辑");
        };

        System.out.println("t2正在等待消息......");
        //消息接收
        channel.basicConsume(QUEUE_NAME,true,deliverCallback,cancelCallback);
    }
}
