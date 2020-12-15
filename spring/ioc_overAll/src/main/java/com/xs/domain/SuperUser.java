package com.xs.domain;

import com.xs.annotation.Super;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @program: learn_root
 * @description:
 * @author: xs-shuai.com
 * @create: 2020-11-11 22:18
 **/
@Super
public class SuperUser extends User{
    
    private String address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "SuperUser{" +
                "address='" + address + '\'' +
                "} " + super.toString();
    }
}
