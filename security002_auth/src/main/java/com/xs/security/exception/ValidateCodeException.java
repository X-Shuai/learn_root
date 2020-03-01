package com.xs.security.exception;


import org.springframework.security.core.AuthenticationException;

/**
 * @program: security_root
 * @description: 验证码异常
 * @author: xs-shuai.com
 * @create: 2020-02-27 14:18
 **/

public class ValidateCodeException extends AuthenticationException {

    public ValidateCodeException(String explanation) {
        super(explanation);
    }
}
