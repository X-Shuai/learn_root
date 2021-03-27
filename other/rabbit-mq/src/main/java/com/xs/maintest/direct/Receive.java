package com.xs.maintest.direct;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @program: learn_root
 * @description:
 * @author: xs-shuai.com
 * @create: 2021-03-27 22:38
 **/
public class Receive {

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
            //创建队列
            channel.queueDeclare("myDirectQueue", true, false, false, null);
            //声明交换机
            channel.exchangeDeclare("directExchange", "direct", true);
            //绑定交换机
            channel.queueBind("myDirectQueue", "directExchange", "directRoutingKey");

            /**
             * 监听某个队列并获取队列中的数据
             * 注意：
             *      当前被监听的队列必须已经存在并正确绑定到了某个交换机中
             */
            channel.basicConsume("myDirectQueue", true, new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag,
                                           Envelope envelope,
                                           AMQP.BasicProperties properties,
                                           byte[] body)
                        throws IOException {
                    String message = new String(body, "utf-8");
                    System.out.println("接受到的消息为" + message);
                }
            });


        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        } finally {
            //关闭资源
            if (channel != null) {
                try {
                    channel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }


    }

}
