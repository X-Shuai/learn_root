package com.xs.aop1.dao;

import com.xs.aop1.annotation.Xs;
import org.springframework.stereotype.Repository;

/**
 * @program: learn_root
 * @description:
 * @author: xs-shuai.com
 * @create: 2020-07-05 18:12
 **/
@Repository("indexDao")
public class IndexDao  implements Dao{

    @Xs
    public void query() {
        System.out.println("query>>>>>>>>>>>>>");
    }

    public void query(String s) {
        System.out.println("query String>>>>>>>>>>>>>");
    }
}
