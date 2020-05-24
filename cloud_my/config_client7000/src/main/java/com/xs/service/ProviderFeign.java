package com.xs.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @program: learn_root
 * @description:
 * @author: xs-shuai.com
 * @create: 2020-05-24 23:55
 **/
@FeignClient("provider")
public interface ProviderFeign {
    @GetMapping(value = "/port")
     String getPortInfo();
}
