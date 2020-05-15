package com.xs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @program: learn_root
 * @description: 启动类
 * @author: xs-shuai.com
 * @create: 2020-05-14 00:20
 **/
@SpringBootApplication
@EnableDiscoveryClient
public class Nacos9001 {

    public static void main(String[] args) {
        SpringApplication.run(Nacos9001.class,args);
    }
}

