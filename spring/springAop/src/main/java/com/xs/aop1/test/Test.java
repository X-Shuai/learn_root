package com.xs.aop1.test;

import com.xs.aop1.config.App;
import com.xs.aop1.dao.Dao;
import com.xs.aop1.dao.IndexDao;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import sun.misc.ProxyGenerator;

import java.io.File;

/**
 * @program: learn_root
 * @description: 测试
 * @author: xs-shuai.com
 * @create: 2020-07-05 18:14
 **/
public class Test {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(App.class);
//        Dao bean = applicationContext.getBean(Dao.class);
        Dao bean = (Dao)applicationContext.getBean("indexDao");
        bean.query("s");
        bean.query();
//        Class<?>[] inter = new  Class[]{Dao.class};
//        byte[] xs = ProxyGenerator.generateProxyClass("Xs", inter);
//
//        File file = new File("D:\\test.class");

    }
}
