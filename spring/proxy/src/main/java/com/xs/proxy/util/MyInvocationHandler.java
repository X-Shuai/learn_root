package com.xs.proxy.util;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @program: learn_root
 * @description: handler
 * @author: xs-shuai.com
 * @create: 2020-07-08 22:08
 **/
public class MyInvocationHandler  implements InvocationHandler {

    Object target;

    public MyInvocationHandler(Object target) {
        this.target = target;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("my<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>");

        return method.invoke(target,args);
    }
}
