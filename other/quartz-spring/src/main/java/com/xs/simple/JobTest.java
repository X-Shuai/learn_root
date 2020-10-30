package com.xs.simple;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

/**
 * @program: quartz
 * @description:
 * @author: xs-shuai.com
 * @create: 2020-10-30 10:42
 **/
public class JobTest {


    public static void main(String[] args) {
        try {

            //调度器
            Scheduler defaultScheduler = StdSchedulerFactory.getDefaultScheduler();

            SimpleScheduleBuilder simpleScheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
                    .withIntervalInSeconds(2).repeatForever();
            //触发器   名称 组   开始时间 结束时间  执行周期 执行规则
            TriggerBuilder<Trigger> triggerTriggerBuilder = TriggerBuilder.newTrigger();
            SimpleTrigger simpleTrigger = triggerTriggerBuilder
//                    .withIdentity("name", "group")
                    .startNow()
                    //配置触发器类型
                    .withSchedule(simpleScheduleBuilder).build();

            //任务  名称 组  方法对应的的位置,需要的参数,
            JobDetail job = JobBuilder.newJob(MyJob.class)
                    .withIdentity("jobName", "JobDetail")
                    //参数
                    .usingJobData("data", "jobData")
                    .usingJobData("number",10)
                    .build();

            //注册 把任务和 触发绑定在一起
            defaultScheduler.scheduleJob(job,  simpleTrigger);

            //开始执行
            defaultScheduler.start();

            //结束

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
