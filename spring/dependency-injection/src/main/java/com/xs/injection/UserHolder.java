package com.xs.injection;

import com.xs.domain.User;

/**
 * @program: learn_root
 * @description:
 * @author: xs-shuai.com
 * @create: 2020-11-14 10:20
 **/
public class UserHolder {
    private User user;

    public UserHolder() {
    }

    public UserHolder(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    @Override
    public String toString() {
        return "UserHolder{" +
                "user=" + user +
                '}';
    }
}
