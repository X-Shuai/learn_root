package com.xs.definition;

import com.sun.jnlp.ApiDialog;
import com.xs.domain.User;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import static org.springframework.beans.factory.support.BeanDefinitionBuilder.genericBeanDefinition;

/**
 * @program: learn_root
 * @description: 注册
 * @author: xs-shuai.com
 * @create: 2020-11-12 16:16
 **/
@Import(AnnotationBeanDefinitionDemo.Config.class)
public class AnnotationBeanDefinitionDemo {
    public static void main(String[] args) {
//        注解配置读取规则
        AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext();

        annotationConfigApplicationContext.register(Config.class);
        //API方式注入

        registerUserBeanDefinition(annotationConfigApplicationContext,"xs-user");
        registerUserBeanDefinition(annotationConfigApplicationContext,"");

        // 启动 Spring 应用上下文
        annotationConfigApplicationContext.refresh();

        System.out.println("Config 类型的所有 Beans" + annotationConfigApplicationContext.getBeansOfType(Config.class));
        System.out.println("User 类型的所有 Beans" + annotationConfigApplicationContext.getBeansOfType(User.class));
        // 显示地关闭 Spring 应用上下文
        annotationConfigApplicationContext.close();
    }

    @Component // 定义当前类作为 Spring Bean（组件）
    public static class Config {

        // 1. 通过 @Bean 方式定义

        /**
         * 通过 Java 注解的方式，定义了一个 Bean
         */
        @Bean(name = {"user", "xs-user"})
        public User user() {
            User user = new User();
            user.setAge(22);
            user.setName("xs");
            return user;
        }
    }

    /***
     * API方式注入
     * @param registry
     * @param beanName
     */
    public static void registerUserBeanDefinition(BeanDefinitionRegistry registry, String beanName) {
        BeanDefinitionBuilder beanDefinitionBuilder = genericBeanDefinition(User.class);
        beanDefinitionBuilder
                .addPropertyValue("age", 24)
                .addPropertyValue("name", "shuai");

        // 判断如果 beanName 参数存在时
        if (StringUtils.hasText(beanName)) {
            // 注册 BeanDefinition
            registry.registerBeanDefinition(beanName, beanDefinitionBuilder.getBeanDefinition());
        } else {
            // 非命名 Bean 注册方法
            BeanDefinitionReaderUtils.registerWithGeneratedName(beanDefinitionBuilder.getBeanDefinition(), registry);
        }
    }
}
