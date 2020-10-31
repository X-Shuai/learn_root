package com.xs.proxy.dao;

/**
 * @program: learn_root
 * @description:
 * @author: xs-shuai.com
 * @create: 2020-07-05 18:12
 **/

public class IndexDao  implements Dao{


    public void query() {
        System.out.println("query>>>>>>>>>>>>>");
    }

    public void query(String s) {
        System.out.println("query String>>>>>>>>>>>>>");
    }
}
