package com.xs.controller;

import com.xs.service.ProviderFeign;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @program: learn_root
 * @description:
 * @author: xs-shuai.com
 * @create: 2020-05-24 00:31
 **/
@RestController
public class InfoController {
    @Value("${neo.hello}")
    private String hello;


    @RequestMapping("/hello")
    public String from() {
        return this.hello;
    }

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ProviderFeign providerFeign;

    @RequestMapping("/getport")
    public String get() throws Exception {
        // 使用 Eureka + Ribbon 后，uri 填写服务名称即可
        System.out.printf(">>>>>>>>>>>>>");
        return providerFeign.getPortInfo();


//        return restTemplate.getForObject("http://provider/port",String.class);
    }

}
