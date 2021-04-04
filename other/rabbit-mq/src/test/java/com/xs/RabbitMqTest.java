package com.xs;

import com.xs.config.Config;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @program: learn_root
 * @description:
 * @author: xs-shuai.com
 * @create: 2021-03-24 23:41
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class RabbitMqTest {

    @Resource
    RabbitTemplate rabbitTemplate;


    @Autowired
    private AmqpTemplate amqpTemplate;

    @Test
    public void directExchange() {
        /**
         * 发送消息
         * 参数1 交换机的名字
         * 参数2 routingkey
         * 参数3 为要发送的消息数据
         */
        amqpTemplate.convertAndSend(Config.DIRECT_EXCHANGE, "orange", "orange");
        amqpTemplate.convertAndSend(Config.DIRECT_EXCHANGE, "black", "black");
        amqpTemplate.convertAndSend(Config.DIRECT_EXCHANGE, "green", "green");
    }

    @Test
    public void producerQueue() {
        /**
         * 发送消息
         * 参数1 交换机的名字
         * 参数2 routingkey
         * 参数3 为要发送的消息数据
         */
        amqpTemplate.convertAndSend(Config.PRODUCER_QUEUE,  "orange");
    }
    @Test
    public void publishQueue() {
        /**
         * 发送消息
         * 参数1 交换机的名字
         * 参数2 routingkey
         * 参数3 为要发送的消息数据
         */
        amqpTemplate.convertAndSend(Config.PUBLISH_EXCHANGE_FANOUT, null, "orange");
    }

    @Test
    public void topicQueue() {
        /**
         * 发送消息
         * 参数1 交换机的名字
         * 参数2 routingkey
         * 参数3 为要发送的消息数据
         */
        amqpTemplate.convertAndSend(Config.TOPIC, "xx.orange.xx", "orange");
        amqpTemplate.convertAndSend(Config.TOPIC, "xx.orange.rabbit", "black");
        amqpTemplate.convertAndSend(Config.TOPIC, "lazy.green", "green");
    }

    @Test
    public void lazyQueue() {
        System.out.println("消息发送时间:"+System.currentTimeMillis());
        amqpTemplate.convertAndSend(Config.ORDER_EXCHANGE, Config.ORDER_QUEUE, "111111", message -> {
            // 设置超时时间 3000ms
            message.getMessageProperties().setExpiration("3000");
            return message;
        });
    }


}
