package com.xs.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: learn_root
 * @description:
 * @author: xs-shuai.com
 * @create: 2020-05-24 00:31
 **/
@RestController
public class InfoController {
    @Value("#{serverxs.portxs}")
    private String port;

    @GetMapping("info")
    public String getServerPort(){
        return port;
    }
}
