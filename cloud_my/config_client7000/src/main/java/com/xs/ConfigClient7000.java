package com.xs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @program: learn_root
 * @description: 配置中心客户端
 * @author: xs-shuai.com
 * @create: 2020-05-24 00:20
 **/
@SpringBootApplication
@EnableEurekaClient
public class ConfigClient7000 {
    public static void main(String[] args) {
        SpringApplication.run(ConfigClient7000.class,args);
    }
}
