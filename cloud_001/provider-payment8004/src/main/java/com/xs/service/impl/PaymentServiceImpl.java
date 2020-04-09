package com.xs.service.impl;

import com.xs.dao.PaymentDao;
import com.xs.entity.Payment;
import com.xs.service.PaymentService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @program: learn_root
 * @description:
 * @author: xs-shuai.com
 * @create: 2020-04-02 23:43
 **/
@Service
public class PaymentServiceImpl implements PaymentService {

    //java自带
    @Resource
    PaymentDao paymentDao;

    @Override
    public int create(Payment payment) {
        return paymentDao.create(payment);
    }

    @Override
    public Payment getPaymentById(Long id) {
        return paymentDao.getPaymentById(id);
    }
}
