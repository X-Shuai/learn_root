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
        amqpTemplate.convertAndSend(Config.PUBLISH_EXCHANGE_FANOUT,  "orange");
    }


}
