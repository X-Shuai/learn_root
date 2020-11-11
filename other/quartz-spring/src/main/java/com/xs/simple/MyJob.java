package com.xs.simple;

import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Date;

/**
 * @program: quartz
 * @description: job
 * @author: xs-shuai.com
 * @create: 2020-10-30 10:30
 **/
public class MyJob implements Job {
    @Override
    public void execute(JobExecutionContext job) throws JobExecutionException {
        JobDetail jobDetail = job.getJobDetail();
        String name = jobDetail.getKey().getName();
        String group = jobDetail.getKey().getGroup();

        String data = jobDetail.getJobDataMap().getString("data");
        int number = jobDetail.getJobDataMap().getIntValue("number");

        Temp tmp = Temp.getTmp(number);

        int integer = tmp.getNumber();

        System.out.println("获得number"+integer);
        tmp.setNumber(integer+1);
        System.out.println("job执行,执行时间,"+new Date() +"   name:"+name+"  group:"+group+" data:"+data);
    }
}
