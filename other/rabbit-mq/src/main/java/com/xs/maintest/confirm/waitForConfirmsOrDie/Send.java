package com.xs.maintest.confirm.waitForConfirmsOrDie;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 发送者确认模式：方式二批量确认
 *
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
            String message = "发送者批量确认模式的测试消息";
            //启动发送者确认模式
            channel.confirmSelect();
            channel.basicPublish("directtransactionExchange",
                    "transactionRoutingKey",
                    null,
                    message.getBytes("utf-8"));
            /**
             * waitForConfirmsOrDie批量消息确认，它会同时向服务中确认之前通道中发送的所有的消息是否已经全部成功写入
             * 这个方法没有任何的返回值，如果服务器中有一条消息没有能够成功或向服务器发送确认时服务不可访问都被认定为
             * 消息确认失败，肯恩那个有消息没有发送成功，我们需要进行消息的补发。
             * 如果无法向服务器获取确认信息，那么方法就会抛出InterruptedException异常，这时就需要补发消息到队列
             * waitForConfirmsOrDie方法可以指定一个参数timeout用于等待服务器的确认时间，如果超过这个时间会抛出异常，表示确认失败
             * 需要补发消息
             * 注意：
             *      批量消息确认的速度比普通的消息确认速度要快，但是如果一旦出现了消息补发的情况，我们不能确定具体是哪条消息
             *      没有完成发送，需要将本次发送的消息全部进行补发
             */
            channel.waitForConfirmsOrDie();
            System.out.println("消息发送成功");
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
