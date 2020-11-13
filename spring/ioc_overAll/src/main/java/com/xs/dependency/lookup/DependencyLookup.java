package com.xs.dependency.lookup;

import com.xs.annotation.Super;
import com.xs.domain.User;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Map;

/**
 * @program: learn_root
 * @description:依赖查找
 * @author: xs-shuai.com
 * @create: 2020-11-11 21:43
 **/
public class DependencyLookup {
    public static void main(String[] args) {
        BeanFactory beanFactory = new ClassPathXmlApplicationContext("classpath:/META-INF/dependency-lookup-context.xml");
//        findByType(beanFactory);
//        findByName(beanFactory);
//        findByNameLazy(beanFactory);
//        findByByType(beanFactory);
        findByByAnnotation(beanFactory);


    }


    /**
     * 名称获取
     * @param beanFactory
     */
    private static void findByName(BeanFactory beanFactory) {
        User bean = (User) beanFactory.getBean("user");
        System.out.println("类型获取:"+bean);
    }

    /**
     * 名称获取
     * @param beanFactory
     */
    private static void findByType(BeanFactory beanFactory) {
        User bean = beanFactory.getBean(User.class);
        System.out.println("类型获取:"+bean);
    }

    /**
     * 延迟查找
     * @param beanFactory
     */
    private static void findByNameLazy(BeanFactory beanFactory) {
        ObjectFactory<User> objectFactory = (ObjectFactory<User>) beanFactory.getBean("beanFactory");
        User user = objectFactory.getObject();
        System.out.println("延迟查找：" + user);
    }

    /***
     * 通过
     * @param beanFactory
     */
    private static void findByByType(BeanFactory beanFactory) {
        if (beanFactory instanceof ListableBeanFactory) {
            ListableBeanFactory listableBeanFactory = (ListableBeanFactory) beanFactory;
            Map<String, User> users = listableBeanFactory.getBeansOfType(User.class);
            System.out.println("查找到的所有的 User 集合对象：" + users);
        }
    }
    /***
     * 通过
     * @param beanFactory
     */
    private static void findByByAnnotation(BeanFactory beanFactory) {
        if (beanFactory instanceof ListableBeanFactory) {
            ListableBeanFactory listableBeanFactory = (ListableBeanFactory) beanFactory;
            Map<String, Object> beans = listableBeanFactory.getBeansWithAnnotation(Super.class);
            System.out.println("查找到的所有注解的 集合对象：" + beans);
        }
    }


}
