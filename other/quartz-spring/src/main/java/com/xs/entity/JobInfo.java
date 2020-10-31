package com.xs.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @program: learn_root
 * @description:
 * @author: xs-shuai.com
 * @create: 2020-10-30 15:25
 **/
@Data
public class JobInfo implements Serializable {
    private static final long serialVersionUID = 8026140551673459050L;
    private int id;
    private String jobName;
    private String jobGroup;
    private String jobClassName;
    private String triggerName;
    private String triggerGroup;
    private String cronExpression;
    private String description;
    private String preFireTime;
    private String nextFireTime;
    private String state;
}