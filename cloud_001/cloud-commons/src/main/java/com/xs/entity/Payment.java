package com.xs.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @program: learn_root
 * @description: 支付类
 * @author: xs-shuai.com
 * @create: 2020-04-02 23:10
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Payment implements Serializable {

    private Long id;
    private String  serial;


}
