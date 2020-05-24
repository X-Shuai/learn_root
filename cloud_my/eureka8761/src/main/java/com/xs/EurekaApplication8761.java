package com.xs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @program: learn_root
 * @description: 注册中心
 * @author: xs-shuai.com
 * @create: 2020-05-23 23:55
 **/
@SpringBootApplication
@EnableEurekaServer
public class EurekaApplication8761 {
    public static void main(String[] args) {
        SpringApplication.run(EurekaApplication8761.class,args);
    }


}
