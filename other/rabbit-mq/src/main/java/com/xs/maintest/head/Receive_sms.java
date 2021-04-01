package com.xs.maintest.head;


import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Hashtable;
import java.util.Map;

/**
 * @program: learn_root
 * @description:
 * @author: xs-shuai.com
 * @create: 2021-04-01 22:00
 **/
public class Receive_sms {
    //队列
    private static final String QUEUE_INFORM_SMS = "queue_inform_sms";
    //交换机
    private static final String EXCHANGE_HEADER_INFORM = "exchange_header_inform";

    public static void main(String[] args) {
        //通过连接工厂创建新的连接来和mq建立连接
        ConnectionFactory connectionFactory = new ConnectionFactory();
        //设置参数
        connectionFactory.setHost("xs.com");//ip
        connectionFactory.setPort(5672);//端口
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        //设置虚拟机一个MQ的服务可以设置多个虚拟机,每个虚拟机就相当于一个MQ
        connectionFactory.setVirtualHost("/");
        try {
            //建立新连接
            Connection connection = connectionFactory.newConnection();
            //创建会话通道,生产者和mq服务所有通信都在channel中完成
            Channel channel = connection.createChannel();
            //声明交换机String exchange, BuiltinExchangeType type
            //参数名称:
            // 1.交换机名称
            // 2.交换机类型，
            // fanout:对应的工作模式:Publish/Subscribe
            // topic:对应的工作模式:Topics
            // direct:对应的工作模式:Routing
            // headers:对应的工作模式:Header
            channel.exchangeDeclare(EXCHANGE_HEADER_INFORM, BuiltinExchangeType.HEADERS);
            //进行交换机队列的绑定
            //参数:String queue, String exchange, String routingKey
            // 1. 队列名称
            // 2. 交换机名称
            // 3. 路由key
            Map<String, Object> headers_sms = new Hashtable<String, Object>();
            headers_sms.put("inform_type", "sms");
            channel.queueBind(QUEUE_INFORM_SMS, EXCHANGE_HEADER_INFORM, "", headers_sms);
            //监听队列
            //声明队列String queue, boolean durable, boolean exclusive, boolean autoDelete, Map<String, Object> arguments
            //参数名称:
            // 1.队列名称
            // 2.是否持久化mq重启后队列还在
            // 3.是否独占连接,队列只允许在该连接中访问,如果连接关闭后就会自动删除了,设置true可用于临时队列的创建
            // 4.自动删除,队列不在使用时就自动删除,如果将此参数和exclusive参数设置为true时,就可以实现临时队列
            // 5.参数,可以设置一个队列的扩展参数,比如可以设置队列存活时间
            channel.queueDeclare(QUEUE_INFORM_SMS, true, false, false, null);
            //实现消费方法
            DefaultConsumer defaultConsumer = new DefaultConsumer(channel) {
                /**
                 * 接受到消息后此方法将被调用
                 * @param consumerTag  消费者标签 用来标记消费者的,可以不设置,在监听队列的时候设置
                 * @param envelope  信封,通过envelope可以获取到交换机,获取用来标识消息的ID,可以用于确认消息已接收
                 * @param properties 消息属性,
                 * @param body 消息内容
                 * @throws IOException
                 */
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    super.handleDelivery(consumerTag, envelope, properties, body);
                    // 交换机
                    String exchange = envelope.getExchange();
                    // 路由key
                    String routingKey = envelope.getRoutingKey();
                    // 消息Id mq在channel中用来标识消息的id,可用于确认消息已接受
                    long deliveryTag = envelope.getDeliveryTag();
                    // 消息内容
                    String message = new String(body, StandardCharsets.UTF_8);
                    System.out.println("receive message: " + message);
                }
            };
            // 监听消息String queue, boolean autoAck, Map<String, Object> arguments, Consumer callback
            //参数名称:
            // 1.队列名称
            // 2.自动回复, 当消费者接受消息之后要告诉mq消息已经接受,如果将此参数设置为true表示会自动回复mq,如果设置为false要通过编程去实现了
            // 3.callback 消费方法,当消费者接受到消息之后执行的方法
            channel.basicConsume(QUEUE_INFORM_SMS, true, defaultConsumer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
