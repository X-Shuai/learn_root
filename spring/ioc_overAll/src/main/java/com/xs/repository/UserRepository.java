package com.xs.repository;

import com.xs.domain.User;
import org.springframework.beans.factory.BeanFactory;

import java.util.Collection;

/**
 * @program: learn_root
 * @description:
 * @author: xs-shuai.com
 * @create: 2020-11-11 23:21
 **/
public class UserRepository {

    /**
     * 自定义
     */
    private Collection<User> users;

    /***
     * 内建对象
     */
    private BeanFactory beanFactory;

    public BeanFactory getBeanFactory() {
        return beanFactory;
    }

    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public Collection<User> getUsers() {
        return users;
    }

    public void setUsers(Collection<User> users) {
        this.users = users;
    }


    @Override
    public String toString() {
        return "UserRepository{" +
                "users=" + users +
                '}';
    }
}
