package com.xs.aop1.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @program: learn_root
 * @description: aop
 * @author: xs-shuai.com
 * @create: 2020-07-05 17:42
 **/
@Configuration
@ComponentScan("com.xs.aop1")
//@EnableAspectJAutoProxy(exposeProxy = false) JDk的动态代理
@EnableAspectJAutoProxy(exposeProxy = true)
public class App {
}
