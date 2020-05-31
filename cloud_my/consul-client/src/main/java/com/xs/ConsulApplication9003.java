package com.xs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @program: learn_root
 * @description: 启动类
 * @author: xs-shuai.com
 * @create: 2020-05-29 22:53
 **/

@SpringBootApplication
@EnableDiscoveryClient
public class ConsulApplication9003 {
    public static void main(String[] args) {
        SpringApplication.run(ConsulApplication9003.class,args);
    }

}
