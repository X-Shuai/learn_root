package com.xs.config;

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
    public RestTemplate getResetTemplate(){
        return new RestTemplate();
    }
}
