package com.xs.definition;

import com.xs.domain.User;
import com.xs.factory.UserFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Iterator;
import java.util.ServiceLoader;

import static java.util.ServiceLoader.load;

/**
 * @program: learn_root
 * @description: 实例化
 * @author: xs-shuai.com
 * @create: 2020-11-12 22:11
 **/
public class SpecialBeanInstantiationDemo {
    public static void main(String[] args) {

        // 配置 XML 配置文件
        // 启动 Spring 应用上下文
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:/META-INF/special-bean-instantiation-context.xml");
        AutowireCapableBeanFactory autowireCapableBeanFactory = applicationContext.getAutowireCapableBeanFactory();

         // 通过 ApplicationContext 获取 AutowireCapableBeanFactory
        ServiceLoader<UserFactory> serviceLoader = autowireCapableBeanFactory.getBean("userFactoryServiceLoader", ServiceLoader.class);

        demoServiceLoader();
        displayServiceLoader(serviceLoader);
    }


    public static void demoServiceLoader() {
        ServiceLoader<UserFactory> serviceLoader = load(UserFactory.class, Thread.currentThread().getContextClassLoader());
        displayServiceLoader(serviceLoader);
    }

    public static void displayServiceLoader(ServiceLoader<UserFactory> serviceLoader){
        Iterator<UserFactory> iterator = serviceLoader.iterator();
        while(iterator.hasNext()){
            System.out.println(iterator.next().createUser());
        }

    }
}
