package com.xs.factory;

import org.springframework.beans.factory.InitializingBean;

import javax.annotation.PostConstruct;

/**
 * @program: learn_root
 * @description: 工厂类
 * @author: xs-shuai.com
 * @create: 2020-11-12 22:29
 **/
public class DefaultUserFactory  implements UserFactory, InitializingBean{


    @PostConstruct
    public void init(){
        System.out.println("init方法执行......");
    }

    /**
     * @Bean(initMethod = "initUserFactory")
     */
    public void initUserFactory(){
        System.out.println("initUserFactory方法执行......");
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("afterPropertiesSet....初始化了");
    }
}
