package com.xs.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: learn_root
 * @description:
 * @author: xs-shuai.com
 * @create: 2021-04-05 01:29
 **/
@Configuration
public class LazyConfig {




    /**
     * 订单exchange
     */
    @Bean
    public DirectExchange orderExchange(){
        return new DirectExchange(Config.ORDER_EXCHANGE,true,false,null);
    }

    /**
     * 订单队列
     */
    @Bean
    public Queue orderQueue() {
        // 设置超时转发策略 超时后消息会通过x-dead-letter-exchange 转发到x-dead-letter-routing-key绑定的队列中
        Map<String, Object> arguments = new HashMap<>(2);

        QueueBuilder durable = QueueBuilder.durable(Config.ORDER_QUEUE);
        durable.withArgument("x-dead-letter-exchange", Config.ORDER_EXCHANGE); // DLX
        durable.withArgument("x-dead-letter-routing-key",Config.ORDER_TIMEOUT_QUEUE); // dead letter携带的routing key
//        durable.withArgument("x-message-ttl", QUEUE_EXPIRATION) // 设置队列的过期时间

        return durable.build();
    }

    /**
     * 超时订单队列
     * @return
     */
    @Bean
    public Queue orderTimeoutQueue() {
        Queue queue = new Queue(Config.ORDER_TIMEOUT_QUEUE,true,false,false);
        return queue;
    }

    /**
     * 订单队列绑定exchange
     * @return
     */
    @Bean
    public Binding orderQueueBinding() {
        return BindingBuilder.bind(orderQueue()).to(orderExchange()).with(Config.ORDER_QUEUE);
    }


    /**
     * 超时订单队列绑定exchange
     * @return
     */
    @Bean
    public Binding  orderTimeoutQueueBinding() {
        return BindingBuilder.bind(orderTimeoutQueue()).to(orderExchange()).with(Config.ORDER_TIMEOUT_QUEUE);
    }
}
