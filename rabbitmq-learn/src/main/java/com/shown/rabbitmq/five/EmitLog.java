package com.shown.rabbitmq.five;

import com.rabbitmq.client.Channel;
import com.shown.rabbitmq.utils.RabbitmqInstance;

import java.util.Scanner;

public class EmitLog {

    public static final String EXCHANGE_NAME = "shown";

    public static void main(String[] args) throws Exception{
        Channel channel = RabbitmqInstance.getChannel();

//        channel.exchangeDeclare(EXCHANGE_NAME,"fanout");

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()){
            String message = scanner.next();
            channel.basicPublish(EXCHANGE_NAME,"",false,false,null,message.getBytes());
            System.out.println("发布的消息为：" + message);
        }
    }
}
