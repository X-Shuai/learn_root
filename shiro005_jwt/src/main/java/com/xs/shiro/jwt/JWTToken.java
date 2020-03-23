package com.xs.shiro.jwt;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * @program: learn_root
 * @description:
 * @author: xs-shuai.com
 * @create: 2020-03-23 23:47
 **/
public class JWTToken implements AuthenticationToken {

    // 密钥
    private String token;

    public JWTToken(String token) {
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }

}