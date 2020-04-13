package com.xs.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @program: learn_root
 * @description: 配置
 * @author: xs-shuai.com
 * @create: 2020-04-06 21:50
 **/

@Configuration
public class ApplicationContextConfig {

    @Bean
    @LoadBalanced //负载均很
    public RestTemplate getResetTemplate(){
        return new RestTemplate();
    }
}
