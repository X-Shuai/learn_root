package com.xs.maintest.confirm.waitForConfirms;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @date 2021/2/21 16:58
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
            String message = "普通发送者确认模式的测试消息";
            //启动发送者确认模式
            channel.confirmSelect();
            channel.basicPublish("directtransactionExchange",
                    "transactionRoutingKey",
                    null,
                    message.getBytes("utf-8"));
            /**
             * 阻塞 线程等待服务返回响应，用于是否消息发送成功，如果服务确认消息已经发送完成则返回true否则返回false
             * 可以为waitForConfirms这个方法指定一个毫秒值关于确定我们需要等待服务响应的超时时间
             * 如果超过了指定的时间后则会抛出异常InterruptedException 表示服务器出现了问题，
             * 需要补发消息或将消息缓存到redis中稍后利用定时任务补发
             * 无论是返回false还是抛出异常消息都有可能发送成功有可能没有发送成功（这句的意思是我事情已经办成了，你再次询问的时候宕机了，所以回了false或异常；也有可能真没发送成功）
             * 如果我们要求这个消息一定要发送到队列中，例如订单数据，那么我们可以采用消息不发
             * 所谓的补发就是重新发送一次消息，可以使用递归或利用redis+定时任务来完成补发
             */
            boolean flag = channel.waitForConfirms();
            System.out.println("消息发送成功:" + flag);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
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
