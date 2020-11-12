package com.xs.factory;

import com.xs.domain.User;

/**
 * @program: learn_root
 * @description: 工厂方法
 * @author: xs-shuai.com
 * @create: 2020-11-12 22:23
 **/

public interface UserFactory {

    default User createUser(){
        return User.createUser();
    }
}
