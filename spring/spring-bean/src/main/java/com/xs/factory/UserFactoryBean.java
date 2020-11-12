package com.xs.factory;

import com.xs.domain.User;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.FactoryBean;

/**
 * @program: learn_root
 * @description: Factory方式实现
 * @author: xs-shuai.com
 * @create: 2020-11-12 22:41
 **/
public class UserFactoryBean implements FactoryBean {
    @Override
    public Object getObject() throws Exception {
        return User.createUser();
    }

    @Override
    public Class<?> getObjectType() {
        return User.class;
    }
}
