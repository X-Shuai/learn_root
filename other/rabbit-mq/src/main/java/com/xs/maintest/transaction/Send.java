package com.xs.maintest.transaction;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 关于事务的消息发送
 *
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
            channel.queueDeclare("transactionQueue", true, false, false, null);
            //声明交换机
            channel.exchangeDeclare("directtransactionExchange", "direct", true);
            //绑定交换机
            channel.queueBind("transactionQueue", "directtransactionExchange", "transactionRoutingKey");
            //发送消息
            String message = "事务的测试消息";

            //启动一个事务，启动事务以后所有写入到队列中的消息，必须显示的调用txCommit()提交事务或txRollback()回滚事务。
            channel.txSelect();
            channel.basicPublish("directtransactionExchange",
                    "transactionRoutingKey",
                    null,
                    message.getBytes("utf-8"));

            /**提交事务，如果我们调用txSelect()方法启动了事务，那么必须显示的调用事务的提交
            否则消息不会真正写入到队列，提交后会将内存中的消息写入队列并释放内存
             */
            channel.txCommit();
            System.out.println("消息发送成功....");

        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }finally {
            //关闭资源
            if (channel != null) {
                try {
                    //回滚事务，放弃当前事务中没有提交的消息，释放内存
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
