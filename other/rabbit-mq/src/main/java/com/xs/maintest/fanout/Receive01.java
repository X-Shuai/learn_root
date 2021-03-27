package com.xs.maintest.fanout;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
/**
 * @program: learn_root
 * @description:
 * @author: xs-shuai.com
 * @create: 2021-03-27 22:40
 **/
public class Receive01 {

    public static void main(String[] args) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("xs.com");
        factory.setPort(5672);
        factory.setUsername("guest");
        factory.setPassword("guest");
        Connection connection = null;
        Channel channel = null;
        try {
            //创建连接
            connection = factory.newConnection();
            //创建通道
            channel = connection.createChannel();

            /**
             * 由于fanout类型的交换机的消息时类似于广播的模式，它不需要绑定RoutingKey
             * 而又可能会又多个消费者来接收这个交换机中的数据，因此我门窗黄健队列时候要
             * 创建一个随机的队列名称
             *
             * 没有参数的queueDeclare方法会创建一个名字为随机的一个队列
             * 这个队列的数据是非持久化
             * 是排外的(同时最多只允许有一个消费者监听当前队列)
             * 自动删除的，当没有任何消费者监听队列时这个队列会自动删除
             *
             * getQueue() 方法用于获取这个随机的队列名
             */
            String queueName = channel.queueDeclare().getQueue();
            //声明交换机
            channel.exchangeDeclare("fanoutExchange", "fanout", true);
            //将这个随机的队列绑定到交换机中，由于是fanout类型的交换机因此不需要指定RountingKey进行绑定
            channel.queueBind(queueName, "fanoutExchange", "");

            /**
             * 监听上面创建的随机队列
             */
            channel.basicConsume(queueName, true, new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag,
                                           Envelope envelope,
                                           AMQP.BasicProperties properties,
                                           byte[] body)
                        throws IOException {
                    String message = new String(body, "utf-8");
                    System.out.println("Receive01消费者 ---" + message);
                }
            });


        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }


    }
}
