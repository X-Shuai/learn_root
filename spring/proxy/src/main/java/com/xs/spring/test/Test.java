package com.xs.spring.test;

import com.xs.spring.service.IndexService;
import com.xs.spring.util.BeanFactory;

/**
 * @program: learn_root
 * @description:
 * @author: xs-shuai.com
 * @create: 2020-07-09 23:37
 **/
public class Test {
    public static void main(String[] args) {
        BeanFactory beanFactory= new BeanFactory("spring.xml");
        IndexService service =(IndexService)beanFactory.getBean("indexService");
        service.query();
    }
}
