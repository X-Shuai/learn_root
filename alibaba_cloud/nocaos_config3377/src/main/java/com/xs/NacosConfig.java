package com.xs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @program: learn_root
 * @description: 启动类
 * @author: xs-shuai.com
 * @create: 2020-05-16 00:39
 **/
@SpringBootApplication
@EnableDiscoveryClient
public class NacosConfig {
    public static void main(String[] args) {
        SpringApplication.run(NacosConfig.class,args);

    }
}
