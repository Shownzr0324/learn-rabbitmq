package com.shown.rabbitmq.three;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.shown.rabbitmq.utils.RabbitmqInstance;
import com.shown.rabbitmq.utils.SleepUtils;

/**
 * @author shown
 * 消息在手动应答时是不丢失、放回队列中重新消费
 */
public class Work02 {
    public static final String task_queue_name = "ack_queue";

    public static void main(String[] args) throws Exception{
        Channel channel = RabbitmqInstance.getChannel();
        System.out.println("C1等待接收消息处理时间较短..");


        DeliverCallback deliverCallback = (consumerTag,message) -> {
            //沉睡1s
            SleepUtils.sleep(1);
            System.out.println("接收到的消息：" + new String(message.getBody(),"UTF-8"));
            /**
             * 手动应答
             * 1、消息的标记 tag
             * 2、是否批量应答
             * 处理一个应答一个
             */
            channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
        };

        CancelCallback cancelCallback = consumerTag -> {
            System.out.println("消息者取消消费者回调逻辑");
        };

        //在接收消息前设置不公平分发
        int prefetchCount = 1;
        channel.basicQos(prefetchCount);

        //采用手动应答
        boolean autoAck = false;
        channel.basicConsume(task_queue_name,autoAck,deliverCallback,cancelCallback);
    }
}
