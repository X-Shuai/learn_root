package com.xs.rabbit;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @program: learn_root
 * @description:
 * @author: xs-shuai.com
 * @create: 2020-12-16 00:16
 **/
public class Rabbit01Producer {

    public static void main(String[] args) {
        // 1、创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        // 2、设置连接属性
        factory.setHost("192.168.101.10");
        factory.setUsername("guest");
        factory.setPassword("guest");

        Connection connection = null;
        Channel channel = null;

        try {
            // 3、从连接工厂获取连接
            connection = factory.newConnection("生产者");

            // 4、从链接中创建通道
            channel = connection.createChannel();

            // 定义一个持久化的，direct类型交换器
            channel.exchangeDeclare("routing_test", "direct", true);

            // 内存、磁盘预警时用
            System.out.println("按回车继续");
            System.in.read();

            // 消息内容
            String message = "Hello A";
            // 发送持久化消息到routing_test交换器上
            channel.basicPublish("routing_test", "c1", MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
            System.out.println("消息 " + message + " 已发送！");

            // 消息内容
            message = "Hello B";
            // 发送持久化消息到routing_test交换器上
            channel.basicPublish("routing_test", "c2", MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
            System.out.println("消息 " + message + " 已发送！");

            // 内存、池畔预警时用
            System.out.println("按回车结束");
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        } finally {
            // 7、关闭通道
            if (channel != null && channel.isOpen()) {
                try {
                    channel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                }
            }

            // 8、关闭连接
            if (connection != null && connection.isOpen()) {
                try {
                    connection.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
