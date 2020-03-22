package com.xs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;

/**
 * @program: learn_root
 * @description: 启动类
 * @author: xs-shuai.com
 * @create: 2020-03-10 00:38
 **/

@SpringBootApplication
public class OssServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(OssServerApplication.class,args);
    }
}
