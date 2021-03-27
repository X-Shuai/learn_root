package com.xs.maintest.direct;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author qiuhongyu
 * @date 2021/2/6 10:16
 */
public class Send {
    public static void main(String[] args) {
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
            //创建队列
            channel.queueDeclare("myDirectQueue", true, false, false, null);
            /**
             * 声明一个交换机
             * 参数1 为交换机的名称，取值任意
             * 参数2 为交换机的类型，取值为direct、fanout、topic、headers（headers效率低，一般取前三个）
             * 参数3 为是否持久话交换机
             * 注意：
             *  1、声明交换机时如果这个交换机存在则回放弃声明，如果交换机不存在则声明交换机
             *  2、这个代码是可有可无的但是在使用前必须要确保这个交换机被声明
             */
            channel.exchangeDeclare("directExchange", "direct", true);

            /**
             * 绑定交换机
             * 参数1 为队列的名称
             * 参数2 为交换机名称
             * 参数3 为消息的RoutingKey(就是BindingKey) 取值任意
             * 注意：
             *      1、在进行队列和交换机绑定时必须要确保交换机和队列已经成功地的声明
             */
            channel.queueBind("myDirectQueue", "directExchange", "directRoutingKey");

            /**
             * 发送消息到指定队列
             * 参数1 为交换机的名称
             * 参数2 为消息的RoutingKey 如果这个消息的RoutingKey和某个队列与交换机绑定的RoutingKey一致
             *       那么这个消息就回发送到指定的队列中
             * 注意：
             *     1、发送消息时必须确保交换机已经创建并且确保已经正确的绑定到某个队列中
             */
            String message = "direct的测试消息";
            channel.basicPublish("directExchange",
                    "directRoutingKey",
                    null,
                    message.getBytes("utf-8"));
            System.out.println("消息发送成功....");

        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }finally {
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
