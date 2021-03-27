package com.xs.maintest.confirm;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @program: learn_root
 * @description:
 * @author: xs-shuai.com
 * @create: 2021-03-27 22:36
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
            channel.queueDeclare("confirmQueue", true, false, false, null);
            //声明交换机
            channel.exchangeDeclare("directConfirmExchange", "direct", true);
            //绑定交换机
            channel.queueBind("confirmQueue", "directConfirmExchange", "confirmRoutingKey");
            //启动事务
//            channel.txSelect();
            /**
             * 接收消息
             * 参数2 为消息的确认机制，true表示自动消息确认，确认以后消息会从队列中被移除，
             * 当读取完消息以后就会自动确认消息，
             * 注意：
             *      1如果我们只是接收的消息但是还没来得及处理，当前应用就崩溃或在进行处理的时候例如像数据库中
             *      数据但是数据库这时不可用，那么由于消息是自动确认的纳闷这个消息就会在接收完成以后自动从队列中被删除
             *      这就丢失消息
             */
            channel.basicConsume("confirmQueue", false, new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag,
                                           Envelope envelope,
                                           AMQP.BasicProperties properties,
                                           byte[] body)
                        throws IOException {
                    //获取当前消息是否被接受过一次，如果返回值为false表示消息之前没有被接收过，true则表示之前这个消息被接收过，
                    //可能也处理完成，因此我们要进行消息的法重复处理
                    boolean isRedeeliver = envelope.isRedeliver();
                    if (!isRedeeliver) {
                        String message = new String(body, "utf-8");
                        System.out.println("消费者接受到的消息为:" + message);
                        //获取消息的编号，需要根据消息的编号来确认消息
                        long tag = envelope.getDeliveryTag();
                        //获取内部类中的通道
                        Channel c = this.getChannel();
                        /*
                         * 手动确认消息，确认以后表示当前消息已经成功处理了，需要从队列中移除掉
                         * 这个方法应该在当前消息的处理程序全部完成以后执行
                         * 参数1 为消息的序号
                         * 参数2 为是否确认多个，如果为true则表示需要确认小于等于当前编号的所有消息，
                         * false就是单个确认值确认当前消息
                         */
                        c.basicAck(tag, true);
                    }else {
                        //程序来到了这里，表示这个消息之前已经被接收过，需要进行防重复处理
                        //例如查询数据库中是否已经添加了记录或已经修改过了数据
                        //如果经过判断这条没有被处理完成的则需要重新处理消息然后确认掉这条消息。
                        //如果已经处理过了则直接确认消息即可，不需要进行其他处理操作
                        //获取消息的编号，需要根据消息的编号来确认消息
                        long tag = envelope.getDeliveryTag();
                        Channel c = this.getChannel();
                        c.basicAck(tag, false);

                    }
                }
            });


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
