package com.xs.spring.anno;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @program: learn_root
 * @description: 注解
 * @author: xs-shuai.com
 * @create: 2020-07-11 21:19
 **/
@Retention(RetentionPolicy.RUNTIME)
public @interface Xs {
    String value()default "xs-shuai.com";
}
