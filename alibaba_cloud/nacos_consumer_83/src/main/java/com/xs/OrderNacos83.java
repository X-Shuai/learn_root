package com.xs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @program: learn_root
 * @description:
 * @author: xs-shuai.com
 * @create: 2020-05-15 23:15
 **/
@SpringBootApplication
@EnableDiscoveryClient
public class OrderNacos83 {
    public static void main(String[] args) {
        SpringApplication.run(OrderNacos83.class,args);
    }
}
