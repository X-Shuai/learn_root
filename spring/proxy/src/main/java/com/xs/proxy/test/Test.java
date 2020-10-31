package com.xs.proxy.test;

import com.xs.proxy.dao.Dao;
import com.xs.proxy.dao.IndexDao;
import com.xs.proxy.util.MyInvocationHandler;

import java.lang.reflect.Proxy;

/**
 * @program: learn_root
 * @description:
 * @author: xs-shuai.com
 * @create: 2020-07-08 22:07
 **/
public class Test {



    public static void main(String[] args) {
        Dao proxyInstance = (Dao) Proxy.newProxyInstance(Test.class.getClassLoader(), new Class[]{Dao.class}, new MyInvocationHandler(new IndexDao()));
        proxyInstance.query();
        proxyInstance.query("ssss");

    }
}
