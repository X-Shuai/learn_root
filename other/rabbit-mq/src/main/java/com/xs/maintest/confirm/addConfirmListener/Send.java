package com.xs.maintest.confirm.addConfirmListener;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 发送者确认模式：方式二批量确认
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
            channel.queueDeclare("confirmQueue", true, false, false, null);
            //声明交换机
            channel.exchangeDeclare("directConfirmExchange", "direct", true);
            //绑定交换机
            channel.queueBind("confirmQueue", "directConfirmExchange", "confirmRoutingKey");
            //发送消息
            String message = "异步确认模式的测试消息";
            //启动发送者确认模式
            channel.confirmSelect();
            //异步消息确认监听器，需要在发送消息前启动
            channel.addConfirmListener(new ConfirmListener() {
                //消息确认以后的回调方法
                //参数1 为被确认的消息的编号 从1开始自动递增用于标记当前是第几个消息
                //参数2 为当前消息是否同时确认了多个
                public void handleAck(long deliveryTag, boolean multiple) throws IOException {
                    System.out.println("消息被确认了---消息编号" + deliveryTag + "是否确认了多条：" + multiple);
                }

                //消息没有确认的回调方法
                //如果这个方法被执行，表示当前的消息没有确认，需要进行消息补发
                //参数1 为被确认的消息的编号 从1开始自动递增用于标记当前是第几个消息
                //参数2 为当前消息是否同时没有确认多个
                public void handleNack(long deliveryTag, boolean multiple) throws IOException {
                    System.out.println("消息没有被确认---消息编号" + deliveryTag + "是否没有确认多条：" + multiple);
                }
            });
                for (int i = 0; i<10000; i++) {
                    channel.basicPublish("directConfirmExchange",
                            "confirmRoutingKey",
                            null,
                            message.getBytes("utf-8"));

                }
            System.out.println("消息发送成功");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        } finally {
//            //关闭资源
//            if (channel != null) {
//                try {
//                    channel.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                } catch (TimeoutException e) {
//                    e.printStackTrace();
//                }
//            }
//            if (connection != null) {
//                try {
//                    connection.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }

        }
    }

}
