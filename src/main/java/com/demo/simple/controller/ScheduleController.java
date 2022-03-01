package com.demo.simple.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.demo.simple.job.SimpleJobBean;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class ScheduleController {

    @Autowired
    private Scheduler scheduler;

    @PostConstruct
    public void start() {

        JobDetail aggreReqJobDetail = buildJobDetail (SimpleJobBean.class, "SimpleJob", "Just Do", new HashMap ());
        try {
        	log.info ("* Just Do Schedule");
			scheduler.scheduleJob (aggreReqJobDetail, buildJobTrigger ("0 * * * * ?"));
		} catch (SchedulerException e) {
			e.printStackTrace ();
		}
    }

    //String scheduleExp ="0 40 11 * * ?"; 초 분 시 일 월 ?
    public Trigger buildJobTrigger (String scheduleExp) {
        return TriggerBuilder.newTrigger ()
                .withSchedule (CronScheduleBuilder.cronSchedule(scheduleExp)).build();
    }

    public JobDetail buildJobDetail (Class job, String name, String group, Map params) {
        JobDataMap jobDataMap = new JobDataMap ();
        jobDataMap.putAll (params);

        return JobBuilder.newJob (job).withIdentity (name, group)
                .usingJobData (jobDataMap)
                .build ();
    }
}
