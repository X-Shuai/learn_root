package com.xs.rabbit;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @program: learn_root
 * @description: 消费者
 * @author: xs-shuai.com
 * @create: 2020-12-16 00:22
 **/
public class Rabbit01Consumer {

    public static void main(String[] args) {
        new Thread(receive, "c1").start();
        new Thread(receive, "c2").start();
    }
    private static Runnable receive = new Runnable() {
        public void run() {
            // 1、创建连接工厂
            ConnectionFactory factory = new ConnectionFactory();
            // 2、设置连接属性
            factory.setHost("192.168.101.10");
            factory.setUsername("guest");
            factory.setPassword("guest");

            Connection connection = null;
            Channel channel = null;
            final String clientName = Thread.currentThread().getName();
            String queueName = "routing_test_queue";

            try {
                // 3、从连接工厂获取连接
                connection = factory.newConnection("消费者-" + clientName);

                // 4、从链接中创建通道
                channel = connection.createChannel();

                // 定义一个持久化的，direct类型交换器
                channel.exchangeDeclare("routing_test", "direct", true);
                // 定义一个持久化队列
                channel.queueDeclare(queueName, true, false, false, null);

                // 将队列和交换器绑定，第三个参数 routingKey是关键，通过此路由键决定接收谁的消息
                channel.queueBind(queueName, "routing_test", clientName);

                // 定义消息接收回调对象
                DeliverCallback callback = new DeliverCallback() {
                    public void handle(String consumerTag, Delivery message) throws IOException {
                        System.out.println(clientName + " 收到消息：" + new String(message.getBody(), "UTF-8"));
                    }
                };
                // 监听队列
                channel.basicConsume(queueName, true, callback, new CancelCallback() {
                    public void handle(String consumerTag) throws IOException {
                    }
                });

                System.out.println(clientName + " 开始接收消息");
                System.in.read();

            } catch (IOException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            } finally {
                // 8、关闭通道
                if (channel != null && channel.isOpen()) {
                    try {
                        channel.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (TimeoutException e) {
                        e.printStackTrace();
                    }
                }

                // 9、关闭连接
                if (connection != null && connection.isOpen()) {
                    try {
                        connection.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    };


}
