package com.xs.controller;

import com.xs.entity.CommonResult;
import com.xs.entity.Payment;
import com.xs.service.PaymentService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @program: learn_root
 * @description:
 * @author: xs-shuai.com
 * @create: 2020-04-02 23:47
 **/
@RestController
@Log4j2
public class PaymentController {
    @Resource
    private PaymentService paymentService;

    @Value("${server.port}")
    private String port;

    @PostMapping(value = "/payment/create")
    public CommonResult create(@RequestBody Payment payment){
        int i = paymentService.create(payment);
        log.info("插入信息");
        return  CommonResult.okData(i);
    }
    @GetMapping(value = "/payment/{id}")
    public CommonResult get(@PathVariable("id") Long id ){
        Payment payment = paymentService.getPaymentById(id);
        log.info("查询"+port);
        return  CommonResult.okData(payment);
    }
    @GetMapping(value = "/payment/zk")
    public CommonResult getZookeeper( ){
        log.info("查询"+port);
        return  CommonResult.okData(port);
    }



}
