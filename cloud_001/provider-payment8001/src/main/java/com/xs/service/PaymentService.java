package com.xs.service;

import com.xs.entity.Payment;
import org.apache.ibatis.annotations.Param;

public interface PaymentService {

    int create(Payment payment);

    Payment getPaymentById(Long id);

}
