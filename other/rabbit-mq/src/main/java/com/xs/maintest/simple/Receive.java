package com.xs.maintest.simple;

import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * @program: learn_root
 * @description:
 * @author: xs-shuai.com
 * @create: 2021-03-27 22:30
 **/
public class Receive {
    public static void main(String[] args) {

        //创建连级工厂对象
        ConnectionFactory factory = new ConnectionFactory();
        //设置RabbitMq的主机ip
        factory.setHost("xs.com");
        //设置RabbitMq的端口号
        factory.setPort(5672);
        //设置访问用户名和密码
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
            /**
             *   声明队列
             *  参数1：队列名取值任意
             *  参数2：是否为持久化的队列
             *  参数3：为是否排外 如果排外则这个队列只允许一个消费者监听
             *  参数4：是否自动删除了，如果为true则表示当队列中没有消息，也没有消费者连接时就会自动删除这个队列
             *  参数5：队列的一些属性设置通常为null即可
             */
            channel.queueDeclare("myQueue", true, false, false, null);

            /**
             * 接收消息
             * 参数1 为当前消费者需要监听的队列名，队列名必须要与发送时的队列名完全一致，否则接收不到消息
             * 参数2 为消息是否自动确认，true表示自动确认接收完消息后回自动将消息从队列中删除。false要像删除得手动删除
             * 参数3 为消息接收者标签，用于当多个消费者同时监听一个队列时用于确认不同的消费者，通常为空字符串即可
             * 参数4 为消息接收的回调方法，这个方法中具体完成消息的处理代码
             * 注意：使用了basicConsume方法以后，回启动一个线程在持续的监听队列，如果队列中有消息的数据进入则回自动接收消息
             *       因此不能关闭连接和通道对象
             */
            channel.basicConsume("myQueue", true, "", new DefaultConsumer(channel) {
                //消息的具体接收和处理方法
                @Override
                public void handleDelivery(String consumerTag,
                                           Envelope envelope,
                                           AMQP.BasicProperties properties,
                                           byte[] body) throws IOException {

                    String message = new String(body, "utf-8");
                    System.out.println("接收到的消息为" + message);

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
