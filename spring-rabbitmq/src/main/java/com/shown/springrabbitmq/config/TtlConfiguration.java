package com.shown.springrabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class TtlConfiguration {

    //普通交换机
    public static final String X_EXCHANGE = "X";
    public static final String Y_DEAD_EXCHANGE = "Y";

    //死信队列
    public static final String DEAD_QUEUE = "QD";

    //普通队列
    public static final String A_QUEUE = "QA";
    public static final String B_QUEUE = "QB";

    @Bean("xExchange")
    public DirectExchange xExchange(){
        return new DirectExchange(X_EXCHANGE);
    }

    @Bean("yExchange")
    public DirectExchange yExchange(){
        return new DirectExchange(Y_DEAD_EXCHANGE);
    }

    @Bean("queueA")
    public Queue queueA(){
        Map<String,Object> arguments = new HashMap<>();
        //设置死信交换机
        arguments.put("x-dead-letter-exchange",Y_DEAD_EXCHANGE);
        //设置死信RoutingKey
        arguments.put("x-dead-letter-routing-key","YD");
        //存活时间  TTL ms
        arguments.put("x-message-ttl",10000);
        return QueueBuilder.durable(A_QUEUE).withArguments(arguments).build();
    }

    @Bean("queueB")
    public Queue queueB(){
        Map<String,Object> arguments = new HashMap<>();
        //设置死信交换机
        arguments.put("x-dead-letter-exchange",Y_DEAD_EXCHANGE);
        //设置死信RoutingKey
        arguments.put("x-dead-letter-routing-key","YD");
        //存活时间  TTL ms
        arguments.put("x-message-ttl",40000);
        return QueueBuilder.durable(B_QUEUE).withArguments(arguments).build();
    }

    @Bean("queueD")
    public Queue queueD(){
        return QueueBuilder.durable(DEAD_QUEUE).build();
    }

    //绑定

    @Bean
    public Binding queueABindingX(@Qualifier("queueA") Queue queueA,
                                  @Qualifier("xExchange") DirectExchange exchangeX){
        return BindingBuilder.bind(queueA).to(exchangeX).with("XA");
    }


    @Bean
    public Binding queueBBindingX(@Qualifier("queueB") Queue queueB,
                                  @Qualifier("xExchange") DirectExchange exchangeX){
        return BindingBuilder.bind(queueB).to(exchangeX).with("XB");
    }


    @Bean
    public Binding queueDBindingY(@Qualifier("queueD") Queue queueD,
                                  @Qualifier("yExchange") DirectExchange exchangeY){
        return BindingBuilder.bind(queueD).to(exchangeY).with("YD");
    }
}
