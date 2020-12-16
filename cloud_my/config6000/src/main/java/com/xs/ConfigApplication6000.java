package com.xs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @program: learn_root
 * @description: 配置中心
 * @author: xs-shuai.com
 * @create: 2020-05-24 00:03
 **/
@SpringBootApplication
@EnableDiscoveryClient
@EnableConfigServer
public class ConfigApplication6000 {
    public static void main(String[] args) {

        SpringApplication.run(ConfigApplication6000.class,args);
    }
}
