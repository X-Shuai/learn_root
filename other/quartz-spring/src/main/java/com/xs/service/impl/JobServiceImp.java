package com.xs.service.impl;

import com.xs.entity.JobInfo;
import com.xs.service.IJobService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @program: learn_root
 * @description:
 * @author: xs-shuai.com
 * @create: 2020-10-30 15:29
 **/
@Service
@Slf4j
public class JobServiceImp implements IJobService {
    @Autowired
    private Scheduler scheduler;


    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public int isJobExist(JobKey jobKey) {
        int result = 1;
        try {
            JobDetail jobDetail = scheduler.getJobDetail(jobKey);
            List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
            if (jobDetail != null && triggers.size() > 0) {
                result = 1;
            } else if (jobDetail != null && triggers.size() == 0) {
                result = 0;
            } else {
                result = -1;
            }
        } catch (SchedulerException e) {
            result = -1;
            log.info("任务不存在！");
        }
        return result;
    }

    @Override
    public List<JobInfo> getAllJobs() {
        List<JobInfo> jobInfos = new ArrayList<JobInfo>();
        try {
            List<String> groups = scheduler.getJobGroupNames();
            int i = 0;
            for(String group :groups) {
                GroupMatcher<JobKey> groupMatcher = GroupMatcher.groupEquals(group);
                Set<JobKey> jobKeys = scheduler.getJobKeys(groupMatcher);
                for(JobKey jobKey:jobKeys) {
                    JobInfo jobInfo = new JobInfo();
                    JobDetail jobDetail = scheduler.getJobDetail(jobKey);
                    jobInfo.setJobName(jobKey.getName());
                    jobInfo.setJobGroup(jobKey.getGroup());
                    jobInfo.setJobClassName(jobDetail.getJobClass().getName());
                    Trigger jobTrigger = scheduler.getTrigger(TriggerKey.triggerKey(jobKey.getName(),jobKey.getGroup()));
                    if (jobTrigger != null) {
                        Trigger.TriggerState tState = scheduler.getTriggerState(TriggerKey.triggerKey(jobKey.getName(),jobKey.getGroup()));
                        jobInfo.setTriggerName(jobKey.getName());
                        jobInfo.setTriggerGroup(jobKey.getGroup());
                        try {
                            CronTrigger cronTrigger = (CronTrigger)jobTrigger;
                            jobInfo.setCronExpression(cronTrigger.getCronExpression());
                        }catch (Exception e) {
                            log.info("不是CronTrigger");
                        }
                        if (jobTrigger.getNextFireTime() != null) {
                            jobInfo.setNextFireTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(jobTrigger.getNextFireTime()));
                        }
                        jobInfo.setDescription(jobDetail.getDescription());
                        jobInfo.setState(tState.name());
                        jobInfo.setId(i);
                        jobInfos.add(jobInfo);
                        i += 1;
                    } else {
                        jobInfo.setState("OVER");
                        jobInfo.setId(i);
                        jobInfos.add(jobInfo);
                        i += 1;
                    }
                }
            }
        } catch (SchedulerException e) {
            log.error(e.getMessage());
        }
        return jobInfos;
    }

    @Override
    public boolean resumeJob(String jobName, String jobGroup) {
        boolean result = true;
        try {
            scheduler.resumeJob(JobKey.jobKey(jobName,jobGroup));
        } catch (SchedulerException e) {
            result = false;
            log.error(e.getMessage());
        }
        return result;
    }

    @Override
    public boolean pauseJob(String jobName, String jobGroup) {
        boolean result = true;
        try {
            scheduler.pauseJob(JobKey.jobKey(jobName,jobGroup));
        } catch (SchedulerException e) {
            result = false;
            log.error(e.getMessage());
        }
        return result;
    }

    @Override
    public boolean reScheduleJob(String jobName, String jobGroup, String cronExpression) {
        //判断当前状态
        boolean result = true;
        try {
            Trigger.TriggerState triggerState = scheduler.getTriggerState(TriggerKey.triggerKey(jobName,jobGroup));
            CronTrigger cronTriggerOld = (CronTrigger)scheduler.getTrigger(TriggerKey.triggerKey(jobName,jobGroup));
            if (!cronTriggerOld.getCronExpression().equals(cronExpression)){
                CronTrigger cronTriggerNew = TriggerBuilder.newTrigger().withIdentity(jobName,jobGroup)
                        .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression))
                        .build();
                scheduler.rescheduleJob(TriggerKey.triggerKey(jobName,jobGroup),cronTriggerNew);
                if (triggerState.name().equals("PAUSED")) {
                    this.pauseJob(jobName,jobGroup);
                }
            }

        } catch (SchedulerException e) {
            result = false;
            log.error(e.getMessage());
        }
        return result;
    }

    @Override
    public boolean deleteJob(String jobName, String jobGroup) {
        boolean result = true;
        try {
            List<? extends Trigger> triggers = scheduler.getTriggersOfJob(JobKey.jobKey(jobName,jobGroup));
            if (triggers.size() > 0) {
                if (!"PAUSED".equals(scheduler.getTriggerState(TriggerKey.triggerKey(jobName,jobGroup)).name())) {
                    scheduler.pauseTrigger(TriggerKey.triggerKey(jobName,jobGroup));
                }
                scheduler.unscheduleJob(TriggerKey.triggerKey(jobName,jobGroup));
            }
            scheduler.deleteJob(JobKey.jobKey(jobName,jobGroup));
        } catch (SchedulerException e) {
            result = false;
            log.error(e.getMessage());
        }
        return result;
    }
    @Override
    public int addJob(JobInfo jobInfo) {
        int result = 0;
        int isJobExist = this.isJobExist(JobKey.jobKey(jobInfo.getJobName(),jobInfo.getJobGroup()));
        if (isJobExist == 1) {
            result = -1;
            log.info("任务已经存在！");
        } else {
            try {
                JobDetail jobDetail = null;
                if (isJobExist == 0) {
                    jobDetail = scheduler.getJobDetail(JobKey.jobKey(jobInfo.getJobName(),jobInfo.getJobGroup()));
                }else if (isJobExist == -1) {
                    jobDetail = JobBuilder.newJob(
                            (Class<? extends QuartzJobBean>)Class.forName(jobInfo.getJobClassName()))
                            .withIdentity(jobInfo.getJobName(),jobInfo.getJobGroup())
                            .withDescription(jobInfo.getDescription())
                            .storeDurably().build();
                }
                //如果jobInfo的cron表达式为空，则创建常规任务，反之创建周期任务
                if (!StringUtils.isEmpty(jobInfo.getCronExpression())) {
                    CronTrigger cronTrigger = TriggerBuilder.newTrigger()
                            .withIdentity(jobInfo.getTriggerName(),jobInfo.getTriggerGroup())
                            .withSchedule(CronScheduleBuilder.cronSchedule(jobInfo.getCronExpression()))
                            .build();
                    scheduler.scheduleJob(jobDetail,cronTrigger);
                } else {
                    Trigger trigger = TriggerBuilder.newTrigger()
                            .withIdentity(jobInfo.getJobName(),jobInfo.getJobGroup())
                            .startAt(sdf.parse(jobInfo.getNextFireTime()))
                            .withSchedule(SimpleScheduleBuilder.simpleSchedule().
                                    withRepeatCount(0))
                            .build();
                    scheduler.scheduleJob(jobDetail,trigger);
                }

            }catch (ClassNotFoundException e) {
                result = 1;
                log.error("任务对应的Class类不存在");
            } catch (SchedulerException e) {
                result = 2;
                log.error("任务调度失败");
            } catch (ParseException e) {
                result = 3;
                log.error("时间转换出错");
            }
        }
        return result;
    }
}
