package com.xs.service;

import org.springframework.stereotype.Component;

/**
 * @program: learn_root
 * @description:
 * @author: xs-shuai.com
 * @create: 2020-05-26 22:14
 **/
@Component
public class ProviderFeignBack implements ProviderFeign {
    @Override
    public String getPortInfo() {
        return "超时了,端口8080";
    }
}
