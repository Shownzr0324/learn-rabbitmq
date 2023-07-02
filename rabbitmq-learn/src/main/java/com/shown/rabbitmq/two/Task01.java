package com.shown.rabbitmq.two;

import com.rabbitmq.client.Channel;
import com.shown.rabbitmq.utils.RabbitmqInstance;

import java.util.Scanner;

/**
 * @author shown
 * 生产者  发送大量的消息
 */

public class Task01 {
    public static final String QUEUE_NAME = "Hello";

    public static void main(String[] args) throws Exception{
        Channel channel = RabbitmqInstance.getChannel();

        //队列的声明
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
        //从控制台接受信息
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()){
            String message = scanner.next();
            /**
             * 生成一个队列
             * 1.队列名称
             * 2.队列里面的消息是否持久化（磁盘） 默认情况消息存储在内存中
             * 3.该队列是否只供一个消费者进行消费
             * 4.是否自动删除，字后一个消费者断开连接或
             */
            channel.basicPublish("",QUEUE_NAME,null,message.getBytes());
            System.out.println("发送完成:" + message);
        }
    }
}
