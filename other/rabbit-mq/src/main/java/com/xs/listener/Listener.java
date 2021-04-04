package com.xs.listener;

import com.xs.config.Config;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @program: learn_root
 * @description:
 * @author: xs-shuai.com
 * @create: 2021-04-01 22:58
 **/
@Component
public class Listener {

    @RabbitListener(queues=Config.DIRECT_QUEUE_1)
    public  void directQueue1(String message){
        System.out.println("DIRECT_QUEUE_1:监听到的消息为：" + message);
    }

    @RabbitListener(queues=Config.DIRECT_QUEUE_2)
    public  void directQueue2(String message){
        System.out.println("DIRECT_QUEUE_2:监听到的消息为：" + message);
    }

    @RabbitListener(queues=Config.PRODUCER_QUEUE)
    public  void producerQueue(String message){
        System.out.println("PRODUCER_QUEUE:监听到的消息为：" + message);
    }


    @RabbitListener(queues=Config.PUBLISH_QUEUE_1)
    public  void publishQueue1(String message){
        System.out.println("PUBLISH_QUEUE_1:监听到的消息为：" + message);
    }

    @RabbitListener(queues=Config.PUBLISH_QUEUE_2)
    public  void publishQueue2(String message){
        System.out.println("PUBLISH_QUEUE_2:监听到的消息为：" + message);
    }
    @RabbitListener(queues=Config.TOPIC_QUEUE_1)
    public  void topicQueue1(String message){
        System.out.println("TOPIC_QUEUE_1:监听到的消息为：" + message);
    }

    @RabbitListener(queues=Config.TOPIC_QUEUE_2)
    public  void topicQueue2(String message){
        System.out.println("TOPIC_QUEUE_2:监听到的消息为：" + message);
    }

    @RabbitListener(queues = Config.ORDER_TIMEOUT_QUEUE,concurrency="4-10")
    public  void lazyMessage(String message){
        System.out.println("接收到消息的时间"+System.currentTimeMillis());
        System.out.println("ORDER_TIMEOUT_QUEUE:监听到的消息为：" + message);
    }



}
