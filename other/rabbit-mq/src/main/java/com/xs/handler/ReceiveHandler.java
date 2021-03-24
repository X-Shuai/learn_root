package com.xs.handler;

import com.rabbitmq.client.Channel;
import com.xs.config.RabbitmqConfig;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @program: learn_root
 * @description: 监听
 * @author: xs-shuai.com
 * @create: 2021-03-24 23:39
 **/
@Component
public class ReceiveHandler {

    /**监听email队列
     *
     * @param msg
     * @param message
     * @param channel
     */
    @RabbitListener(queues = {RabbitmqConfig.QUEUE_INFORM_EMAIL})
    public void receive_email(String msg, Message message, Channel channel){
        System.out.println(msg);
    }

    /**监听sms队列
     *
     * @param msg
     * @param message
     * @param channel
     */
    @RabbitListener(queues = {RabbitmqConfig.QUEUE_INFORM_SMS})
    public void receive_sms(String msg,Message message,Channel channel){
        System.out.println(msg);
    }
}
