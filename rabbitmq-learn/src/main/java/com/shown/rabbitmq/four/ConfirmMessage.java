package com.shown.rabbitmq.four;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;
import com.rabbitmq.client.DeliverCallback;
import com.shown.rabbitmq.utils.RabbitmqInstance;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * @author shown
 * 发布确认模式
 * 使用的时间 比较哪种确认方式是最好的
 * 1.单个确认
 * 2.批量确认
 * 3.异步批量确认
 *
 */

public class ConfirmMessage {

    //批量发消息的个数
    public static final int MESSAGE_COUNT = 1000;

    public static void main(String[] args) throws Exception {
        //1.单个确认
//        publishMessageIndividually(); //发布1000个单独确认消息，耗时605ms
        //2.批量确认
//        publishMessageBatch();  //发布1000个批量确认消息，耗时178ms
        //3.异步批量确认
        publishMessageAsync(); //发布1000个异步批量确认消息，耗时68ms
//        发布1000个异步批量确认消息，耗时52ms

//        test1();
    }

    //单个确认
    public static void publishMessageIndividually() throws Exception{
        Channel channel = RabbitmqInstance.getChannel();
        //队列声明
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName,false,false,false,null);


        //开启发布确认
        channel.confirmSelect();
        //开始时间
        long begin = System.currentTimeMillis();
        //批量发下消息
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = i + "";
            channel.basicPublish("",queueName,null,message.getBytes());
            //单个消息马上进行发布确认
            boolean flag = channel.waitForConfirms();
            if (flag){
                System.out.println("消息发送成功");
            }
        }
        //结束时间
        long end = System.currentTimeMillis();
        System.out.println("发布" + MESSAGE_COUNT + "个单独确认消息，耗时" + (end - begin) + "ms");
    }

    //批量发布确认
    public static void publishMessageBatch() throws Exception{
        Channel channel = RabbitmqInstance.getChannel();
        //队列声明
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName,false,false,false,null);


        //开启发布确认
        channel.confirmSelect();
        //开始时间
        long begin = System.currentTimeMillis();

        //批量确认消息大小
        int batchSize = 100;

        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = i + "";
            channel.basicPublish("",queueName,null,message.getBytes());

            //判断达到100条消息的时候 批量确认一次
            if (i%batchSize == 0){
                //发布确认
                channel.waitForConfirms();
            }
        }

        //结束时间
        long end = System.currentTimeMillis();
        System.out.println("发布" + MESSAGE_COUNT + "个批量确认消息，耗时" + (end - begin) + "ms");
    }

    //异步批量确认
    public static void publishMessageAsync() throws Exception{
        Channel channel = RabbitmqInstance.getChannel();
        //队列声明
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName,false,false,false,null);


        //开启发布确认
        channel.confirmSelect();

        /**
         * 线程安全有序的一个哈希表，适用于高并发的情况下
         * 1.轻松的将序号与消息进行关联
         * 2.轻松批量删除条目 只要给到序号
         * 3.支持高并发（多线程）
         */
        ConcurrentSkipListMap<Long,String> outstandingConfirms = new ConcurrentSkipListMap<>();

        //开始时间
        long begin = System.currentTimeMillis();
        //消息确认成功 回调函数
        ConfirmCallback ark_confirm = (messageTag , multiple) ->{
            //2：删除 已确认的消息  剩下的就是未确认的消息
            if (multiple){
                //如果是批量
                ConcurrentNavigableMap<Long,String> confirmed = outstandingConfirms.headMap(messageTag);
                confirmed.clear();
            }else {
                outstandingConfirms.remove(messageTag);
            }

            System.out.println("消息确认:" + messageTag);
        };
        //消息确认失败 回调函数
        ConfirmCallback nak_confirm = (messageTag , multiple) -> {
            //3：打印一下未确认的消息
            String message = outstandingConfirms.get(messageTag);
            System.out.println("未确认的消息是：" + message + "-----未确认的消息tag:" + messageTag);
        };
        /**
         * 准备消息的监听器监听哪些消息成功，哪些消息失败
         * 1.监听哪些消息成功
         * 2.监听哪些消息失败
         */
        channel.addConfirmListener(ark_confirm,nak_confirm); //异步通知

        //批量发送
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = i + "";
            channel.basicPublish("",queueName,null,message.getBytes());
            //1：此处记录下所有要发送的消息
            outstandingConfirms.put(channel.getNextPublishSeqNo(),message);
        }

        //结束时间
        long end = System.currentTimeMillis();
        System.out.println("发布" + MESSAGE_COUNT + "个异步批量确认消息，耗时" + (end - begin) + "ms");
    }

    //异步批量确认练习
//    public static void test1() throws Exception{
//        Channel channel = RabbitmqInstance.getChannel();
//
//        //声明队列
//        String queueName = UUID.randomUUID().toString();
//        channel.queueDeclare(queueName,false,false,false,null);
//
//        //定义开始时间
//        long begin = System.currentTimeMillis();
//
//        //开启发布确认
//        channel.confirmSelect();
//
//        //定义一个线程安全的哈希表
//        ConcurrentSkipListMap<Long,String> outMessage = new ConcurrentSkipListMap<>();
//
//        ConfirmCallback confirmCallback = (messageTag,multiple) -> {
//          if (multiple){
//              //如果是批量
//              //headMap用于 返回小于给定键的键的映射视图。 也就是获取到当前确认到的消息的之前都所有消息
//              ConcurrentNavigableMap<Long,String> confirmedMessage = outMessage.headMap(messageTag);
//              //删除已经确认的消息
//              confirmedMessage.clear();
//          }else {
//              outMessage.remove(messageTag);
//          }
//            System.out.println("消息已确认：" + messageTag);
//        };
//
//        ConfirmCallback unConfirmCallback = (messageTag,multiple) -> {
//            //未确认的消息
//            String message = outMessage.get(messageTag);
//            System.out.println("未确认的消息：" + message );
//        };
//
//        //消息监听器
//        channel.addConfirmListener(confirmCallback,unConfirmCallback);
//
//        for (int i = 0; i < MESSAGE_COUNT; i++) {
//            String message = i + "";
//            channel.basicPublish("",queueName,false,null, message.getBytes());
//            //将所有要发送的消息存入哈希表中
//            outMessage.put(channel.getNextPublishSeqNo(), message);
//        }
//
//        //结束时间
//        long end = System.currentTimeMillis();
//        System.out.println("耗时" + (end - begin) + "ms");
//    }
}
