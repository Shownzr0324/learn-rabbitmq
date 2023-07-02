package com.shown.rabbitmq.first;

import com.rabbitmq.client.Channel;
import com.shown.rabbitmq.utils.RabbitmqInstance;

public class Producer {

    public static final String QUEUE_NAME = "Hello";

    public static void main(String[] args) throws Exception{
        Channel channel = RabbitmqInstance.getChannel();
        /**
         * 生成一个队列
         * 1.队列名称
         * 2.队列里面的消息是否持久化（磁盘） 默认情况消息存储在内存中
         * 3.该队列是否只供一个消费者进行消费
         * 4.是否自动删除，字后一个消费者断开连接或
         */

        channel.queueDeclare(QUEUE_NAME,false,false,false,null);

        //发消息
        String message = "Hello!";

        channel.basicPublish("",QUEUE_NAME,null,message.getBytes());
        System.out.println("消息发送成功");
    }
}
