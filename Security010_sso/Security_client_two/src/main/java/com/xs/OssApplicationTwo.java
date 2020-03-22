package com.xs;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;

/**
 * @program: learn_root
 * @description:
 * @author: xs-shuai.com
 * @create: 2020-03-10 00:52
 **/
@EnableOAuth2Sso
@SpringBootApplication
public class OssApplicationTwo {
    public static void main(String[] args) {
        SpringApplication.run(OssApplicationTwo.class,args);
    }
}
