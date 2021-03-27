package com.xs.maintest.transaction;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
/**
 * @program: learn_root
 * @description:
 * @author: xs-shuai.com
 * @create: 2021-03-27 22:44
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
            channel.queueDeclare("transactionQueue", true, false, false, null);
            //声明交换机
            channel.exchangeDeclare("directtransactionExchange", "direct", true);
            //绑定交换机
            channel.queueBind("transactionQueue", "directtransactionExchange", "transactionRoutingKey");
            /**
             * 开启事务
             * 当消费者开启事务后，即使不作事务的提交，依然可以获取队列中的消息并且将消息从队列中移除
             * 注意：
             *      暂时事务对接收者没有影响
             */
            channel.txSelect();
            //监听
            channel.basicConsume("transactionQueue", true, new DefaultConsumer(channel) {
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
                    //回滚，释放内存
                    channel.txRollback();
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
