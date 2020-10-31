package com.xs.controller;

import com.xs.bean.R;
import com.xs.entity.JobInfo;
import com.xs.service.IJobService;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.Response;
import org.quartz.CronExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @program: learn_root
 * @description:
 * @author: xs-shuai.com
 * @create: 2020-10-30 15:35
 **/
@Controller
@RequestMapping(value = "/task")
@Slf4j
public class JobController {

    @Autowired
    private IJobService jobService;

    @GetMapping(value = "/ok")
    @ResponseBody
    public String sendok() {
        return "ok";
    }

    /***
     * 添加
     * @param jobInfo
     * @return
     */
    @PostMapping(value = "/add")
    @ResponseBody
    public R addJob(@RequestBody JobInfo jobInfo) {
        R response = null;
        switch (jobService.addJob(jobInfo)) {
            case -1:
                response = R.buildFail("任务已存在！");
                break;
            case 0:
                response = R.buildOk("success");
                break;
            case 1:
                response = R.buildFail("没有该任务对应的 Java Class 类！");
                break;
            case 2:
                response = R.buildFail("添加任务失败！");
                break;
            case 3:
                response = R.buildFail("时间格式错误！");
                break;
            default:
                response = R.buildFail("其他错误");
                break;
        }
        return response;
    }

    /***
     * 查看所有任务
     * @return
     */
    @GetMapping(value = "/jobs")
    @ResponseBody
    public R getAllJobs() {
        List<JobInfo> jobInfos = jobService.getAllJobs();
        return jobInfos.size() > 0 ?
                R.buildOkData(jobInfos, "success")
                : R.buildFail("errror！");
    }

    /**
     * 停止任务
     * @param name
     * @param group
     * @return
     */
    @PostMapping(value = "/pause")
    @ResponseBody
    public R pauseJob(String name, String group) {
        return jobService.pauseJob(name, group) ?
                R.buildOk("success")
                : R.buildFail("errror！");
    }

    /***
     * 恢复任务
     * @param name
     * @param group
     * @return
     */
    @PostMapping(value = "/resume")
    @ResponseBody
    public R resumeJob(String name, String group) {
        return jobService.resumeJob(name, group) ? R.buildOk("success")
                : R.buildFail("errror！");
    }

    /***
     * 修改任务执行周期表达式
     * @param name
     * @param group
     * @param cron
     * @return
     */
    @PostMapping(value = "/reschedule")
    @ResponseBody
    public R reScheduleJob(String name, String group, String cron) {
        return jobService.reScheduleJob(name, group, cron) ? R.buildOk("success")
                : R.buildFail("errror！");
    }

    /**
     * 删除任务
     * @param name
     * @param group
     * @return
     */
    @PostMapping(value = "/delete")
    @ResponseBody
    public R deleteJob(String name, String group) {
        return jobService.deleteJob(name, group) ? R.buildOk("success")
                : R.buildFail("cron表达式格式错误！");
    }

    /**校验是否是合法cron表达式
     *
     * @param cron
     * @return
     */
    @PostMapping(value = "/cron-check")
    @ResponseBody
    public R checkCron(String cron) {
        boolean valide = false;
        try {
            valide = CronExpression.isValidExpression(cron);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return valide ? R.buildOk("success")
                : R.buildFail("cron表达式格式错误！");
    }
}