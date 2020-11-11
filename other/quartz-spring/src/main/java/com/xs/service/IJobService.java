package com.xs.service;

import com.xs.entity.JobInfo;
import org.quartz.JobKey;

import java.util.List;

/**
 * @program: learn_root
 * @description:
 * @author: xs-shuai.com
 * @create: 2020-10-30 15:27
 **/
public interface IJobService {
    /**查询所有任务
     * 
     * @return
     */
      List<JobInfo> getAllJobs();

    /**恢复任务
     * 
     * @param jobName
     * @param jobGroup
     * @return
     */
      boolean resumeJob(String jobName,String jobGroup);

    /**停止任务
     * 
     * @param jobName
     * @param jobGroup
     * @return
     */
      boolean pauseJob(String jobName,String jobGroup);

    /**修改任务执行周期表达式
     * 
     * @param jobName
     * @param jobGroup
     * @param cronExpression
     * @return
     */
      boolean reScheduleJob(String jobName,String jobGroup,String cronExpression);
    /**删除任务
     */
      boolean deleteJob(String jobName,String jobGroup);

    /**新增任务
     * 
     * @param jobInfo
     * @return
     */
      int addJob(JobInfo jobInfo);

    /**判断任务是否存在
     * 
     * @param jobKey
     * @return
     */
      int isJobExist(JobKey jobKey);

}
