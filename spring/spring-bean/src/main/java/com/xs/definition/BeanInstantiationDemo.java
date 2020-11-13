package com.xs.definition;

import com.xs.domain.User;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @program: learn_root
 * @description: 实例化
 * @author: xs-shuai.com
 * @create: 2020-11-12 22:11
 **/
public class BeanInstantiationDemo {
    public static void main(String[] args) {

        BeanFactory beanFactory = new ClassPathXmlApplicationContext("classpath:/META-INF/bean-instantiation-context.xml");
        User user1 = beanFactory.getBean("user-by-static-method",User.class);
        User user2 = beanFactory.getBean("user-by-instance-method",User.class);
        User user3 = beanFactory.getBean("user-by-factory-bean",User.class);
        System.out.println(user1);
        System.out.println(user2);
        System.out.println(user3);

        System.out.println("user1等于user2"+(user1==user2));
    }
}
