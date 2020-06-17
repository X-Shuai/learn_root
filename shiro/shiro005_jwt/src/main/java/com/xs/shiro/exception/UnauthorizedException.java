package com.xs.shiro.exception;

/**
 * @program: learn_root
 * @description: 自定义异常
 * @author: xs-shuai.com
 * @create: 2020-03-23 23:30
 **/
public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String msg) {
        super(msg);
    }

    public UnauthorizedException() {
        super();
    }
}

