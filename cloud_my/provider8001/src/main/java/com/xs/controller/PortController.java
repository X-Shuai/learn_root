package com.xs.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: learn_root
 * @description:
 * @author: xs-shuai.com
 * @create: 2020-05-24 23:41
 **/
@RestController
public class PortController {

    @Value("${server.port}")
    private String port;

    @RequestMapping("/port")
    public String getPortInfo() {
        return this.port;
    }

}
