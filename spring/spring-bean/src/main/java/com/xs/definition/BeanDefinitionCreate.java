package com.xs.definition;

import com.xs.domain.User;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.GenericBeanDefinition;

/**
 * @program: learn_root
 * @description: BeanDefinition构建实例
 * @author: xs-shuai.com
 * @create: 2020-11-12 15:00
 * {@link  org.springframework.beans.factory.config.BeanDefinition}
 **/
public class BeanDefinitionCreate {

    public static void main(String[] args) {

        //1. BeanDefinitionBuilder
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(User.class);
        //设置属性
        beanDefinitionBuilder
                .addPropertyValue("name","xs")
                .addPropertyValue("age",20);
//        beanDefinitionBuilder.addPropertyValue("name","xs");
//        beanDefinitionBuilder.addPropertyValue("age",20);
        //得到beanDefinition
        AbstractBeanDefinition beanDefinition = beanDefinitionBuilder.getBeanDefinition();

        //2.过 AbstractBeanDefinition 以及派生类
        GenericBeanDefinition genericBeanDefinition = new GenericBeanDefinition();
        //定义bean类型
        genericBeanDefinition.setBeanClass(User.class);
        //设置属性
        MutablePropertyValues propertyValues = new MutablePropertyValues();
        propertyValues.add("name","xs")
                .add("age",20);
        //批量设置属性
        genericBeanDefinition.setPropertyValues(propertyValues);

    }
}
