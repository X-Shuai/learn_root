package com.xs.spring.service;

import com.xs.spring.anno.Xs;
import org.springframework.stereotype.Service;

/**
 * @program: learn_root
 * @description:
 * @author: xs-shuai.com
 * @create: 2020-07-11 21:17
 **/
@Xs
public class IndexServiceImpl  implements IndexService{

    public String query() {
        System.out.println("query");
        return "query";
    }
}

