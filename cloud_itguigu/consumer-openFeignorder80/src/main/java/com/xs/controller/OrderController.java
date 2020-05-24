package com.xs.controller;

import com.xs.entity.CommonResult;
import com.xs.entity.Payment;
import com.xs.service.OrderService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

/**
 * @program: learn_root
 * @description: 订单描述
 * @author: xs-shuai.com
 * @create: 2020-04-06 21:42
 **/
@RestController
public class OrderController {
    @Resource
    OrderService orderService;

    @GetMapping("/consumer/payment/create")
    public CommonResult create(Payment payment){
        return orderService.create(payment);
    }
    @GetMapping("/consumer/payment/{id}")
    public CommonResult getPayment(@PathVariable("id") Long id) {
        return orderService.get(id);
    }



}
