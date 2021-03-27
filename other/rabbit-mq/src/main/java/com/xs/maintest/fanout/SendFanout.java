package com.xs.maintest.fanout;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author qiuhongyu
 * @date 2021/2/7 11:25
 */
public class SendFanout {
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
            /**
             * 由于使用Fanout类型的交换机，因此消息的接收方可能会有多个，因此不建议在消息发送时来创建队列
             * 以及绑定交换机,建议在消费者中创建队列并绑定交换机。
             * 但是发送消息时至少应该确保交换机是存在的   channel.exchangeDeclare。
             */
//            channel.queueDeclare("myDirectQueue", true, false, false, null);
//            channel.queueBind("myDirectQueue", "directExchange", "directRoutingKey");
              channel.exchangeDeclare("fanoutExchange", "fanout", true);

            /**
             * 发送消息到指定队列
             * 参数1 为交换机的名称
             * 参数2 为消息的RoutingKey 如果这个消息的RoutingKey和某个队列与交换机绑定的RoutingKey一致
             *       那么这个消息就回发送到指定的队列中。fanout类型不用routingkey，给""即可
             */
            String message = "fanout的测试消息";
            channel.basicPublish("fanoutExchange",
                    "",
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
