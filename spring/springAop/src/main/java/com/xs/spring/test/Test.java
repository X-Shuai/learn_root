package com.xs.spring.test;

import com.xs.spring.util.AnnotationConfigApplicationContext;

/**
 * @program: learn_root
 * @description:
 * @author: xs-shuai.com
 * @create: 2020-07-11 21:16
 **/
public class Test {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.scan("com.xs.spring.service");
    }
}
