package com.xs.dependecy;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @program: learn_root
 * @description: 依赖查找
 * @author: xs-shuai.com
 * @create: 2020-11-13 10:48
 **/

public class DependencyLookUp {
    //Configuration不是必须的注解
    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        //当前类为平配置类
        applicationContext.register(DependencyLookUp.class);
        applicationContext.refresh();
        lookupByObjectProvider(applicationContext);
        //查找
    }

    @Bean
    public String helloWord(){
        return "hello,word";
    }
    public static  void lookupByObjectProvider(ApplicationContext applicationContext){
        ObjectProvider<String> beanProvider = applicationContext.getBeanProvider(String.class);
        System.out.println(beanProvider.getObject());
    }

}
