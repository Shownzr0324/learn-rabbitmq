package com.shown.rabbitmq.three;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import com.shown.rabbitmq.utils.RabbitmqInstance;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * @author shown
 * 消息在手动应答时是不丢失、放回队列中重新消费
 */
public class Task02 {

    public static final String task_queue_name = "ack_queue";

    public static void main(String[] args) throws Exception{
        Channel channel = RabbitmqInstance.getChannel();

        //声明队列
        channel.queueDeclare(task_queue_name,true,false,false,null);
        //从控制台中输入信息
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()){
            String message = scanner.next();
            channel.basicPublish("",task_queue_name , null,message.getBytes(StandardCharsets.UTF_8));
            System.out.println("生产者发出消息:" + message);
        }
    }
}
