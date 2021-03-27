package com.xs.maintest.simple;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @program: learn_root
 * @description:
 * @author: xs-shuai.com
 * @create: 2021-03-27 22:32
 **/
public class Send {

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
             *  创建队列，自定义名字为myQueue
             *  参数1：队列名取值任意
             *  参数2：是否为持久化的队列
             *  参数3：为是否排外 如果排外则这个队列只允许一个消费者监听
             *  参数4：是否自动删除了，如果为true则表示当队列中没有消息，也没有消费者连接时就会自动删除这个队列
             *  参数5：队列的一些属性设置通常为null即可
             *  注意：
             *      1、声明队列时，这个队列名称如果已经存在则放弃声明，如果队列不存在则回声明一个新的队列
             *      2、队列名可以取任意值，但是要与消息接收时完全一致
             *      3、这行代码可有可无，但是一定要在发送消息前确认队列名已经存在在RabbitMq中，否则回抛异常
             */
            channel.queueDeclare("myQueue", true, false, false, null);

            /**
             *  发送消息到指定队列中
             *  参数1 为交换机名称 这里为空字符串表示不使用交换机
             *  参数2 为队列名或Routing,当指定了交换机名称后这个值就是RoutingKey
             *  参数3 为消息的属性信息 通常为空
             *  参数4 为具体的消息数据的字节数组
             */
            String message = "hello World!";
            channel.basicPublish("", "myQueue", null, message.getBytes("utf-8"));
            System.out.println("消息发送成功:" + message);
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
