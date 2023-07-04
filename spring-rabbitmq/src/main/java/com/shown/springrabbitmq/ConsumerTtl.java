package com.shown.springrabbitmq;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author shown
 * 队列TTL消费者
 */
@Slf4j
@Component
public class ConsumerTtl {

    //接收消息
    @RabbitListener(queues = "QD")
    public void ReceivedD(Message message, Channel channel){
        String msg = new String(message.getBody());
        log.info("当前时间:{},收到死信队列的消息:{}", new Date(),msg);
    }
}
