package com.shown.rabbitmq.first;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.shown.rabbitmq.utils.RabbitmqInstance;

/**
 * @author shown
 * 接收消息
 */
public class Consume {

    public static final String QUEUE_NAME = "Hello";

    public static void main(String[] args) throws Exception{
        Channel channel = RabbitmqInstance.getChannel();

        //声明 接收消息
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println(new String(message.getBody()));
        };

        //取消消息时的回调
        CancelCallback cancelCallback = consumerTag -> {
            System.out.println("消息消费被中断");
        };

        /**
         * 消费者消费信息
         * 1.消费哪个队列
         * 2.消费成功之后是否要自动应答 true 代表的自动应答 ，false 代表手动应答
         * 3.消费者未成功消费的回调
         * 4.消费者取消消费的回调
         */
        channel.basicConsume(QUEUE_NAME,true,deliverCallback,cancelCallback);
    }


}
