package com.xs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @program: learn_root
 * @description:
 * @author: xs-shuai.com
 * @create: 2020-05-05 23:38
 **/
@SpringBootApplication
@EnableEurekaClient
public class GateWay9527
{
    public static void main(String[] args) {
        SpringApplication.run(GateWay9527.class,args);
    }
}
