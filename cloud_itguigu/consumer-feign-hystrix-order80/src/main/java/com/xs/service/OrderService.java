package com.xs.service;

import com.xs.entity.CommonResult;
import com.xs.entity.Payment;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @program: learn_root
 * @description:
 * @author: xs-shuai.com
 * @create: 2020-04-19 17:19
 **/
@Component
@FeignClient(value = "PRIVDER-PAYMENT-SERVICE" )
public interface OrderService {

    @GetMapping(value = "/payment/{id}")
     CommonResult get(@PathVariable("id") Long id );

    @PostMapping(value = "/payment/create")
    CommonResult create(@RequestBody Payment payment);

}
