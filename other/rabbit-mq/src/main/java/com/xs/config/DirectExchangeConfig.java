package com.xs.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @program: learn_root
 * @description: mq配置
 * @author: xs-shuai.com
 * @create: 2021-03-24 23:35
 **/
@Configuration
public class DirectExchangeConfig {
    @Bean
    public DirectExchange directExchange(){
        DirectExchange directExchange=new DirectExchange(Config.DIRECT_EXCHANGE);
        return directExchange;
    }

    @Bean
    public Queue directQueue1() {
        Queue queue=new Queue(Config.DIRECT_QUEUE_1);
        return queue;
    }

    @Bean
    public Queue directQueue2() {
        Queue queue=new Queue(Config.DIRECT_QUEUE_2);
        return queue;
    }

    //3个binding将交换机和相应队列连起来
    @Bean
    public Binding bindingorange(){
        Binding binding=BindingBuilder.bind(directQueue1()).to(directExchange()).with("orange");
        return binding;
    }

    @Bean
    public Binding bindingblack(){
        Binding binding=BindingBuilder.bind(directQueue2()).to(directExchange()).with("black");
        return binding;
    }

    @Bean
    public Binding bindinggreen(){
        Binding binding=BindingBuilder.bind(directQueue2()).to(directExchange()).with("green");
        return binding;
    }



}