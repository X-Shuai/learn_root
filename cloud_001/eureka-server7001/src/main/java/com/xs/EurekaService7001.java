package com.xs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @program: learn_root
 * @description: 启动类
 * @author: xs-shuai.com
 * @create: 2020-04-08 00:15
 **/
@SpringBootApplication
@EnableEurekaServer
public class EurekaService7001 {
    public static void main(String[] args) {
        SpringApplication.run(EurekaService7001.class,args);
    }
}
