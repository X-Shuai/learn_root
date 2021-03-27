package com.xs.maintest.topic;

import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * @program: learn_root
 * @description:
 * @author: xs-shuai.com
 * @create: 2021-03-27 22:41
 **/
public class Receive02 {
    public static void main(String[] args) {
        //创建连级工厂对象
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("xs.com");
        factory.setPort(5672);
        factory.setUsername("guest");
        factory.setPassword("guest");
        //定义连接对象
        Connection connection = null;
        //定义通道对象
        Channel channel = null;
        try {
            //获取连接
            connection = factory.newConnection();
            //获取通道
            channel = connection.createChannel();
            //声明队列
            channel.queueDeclare("topicQueue02", true, false, false, null);
            //声明交换机
            channel.exchangeDeclare("topicExchange", "topic", true);
            //绑定
            channel.queueBind("topicQueue02", "topicExchange", "aa.*");
            //接收消息
            channel.basicConsume("topicQueue02", true, "", new DefaultConsumer(channel) {
                //消息的具体接收和处理方法
                @Override
                public void handleDelivery(String consumerTag,
                                           Envelope envelope,
                                           AMQP.BasicProperties properties,
                                           byte[] body) throws IOException {

                    String message = new String(body, "utf-8");
                    System.out.println("Receive02消费者aa.*---" + message);

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
