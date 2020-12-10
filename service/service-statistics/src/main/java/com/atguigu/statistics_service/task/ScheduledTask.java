package com.atguigu.statistics_service.task;

import com.atguigu.statistics_service.service.StatisticsDailyService;
import com.atguigu.statistics_service.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @Author Administrator
 * @CreateTime 2020-12-9
 * @Description
 */
@Component
public class ScheduledTask {

    @Autowired
    private StatisticsDailyService statisticsDailyService;


    /**
     * 定时，每天凌晨一点执行统计上一天的数据
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void dailiesTask() {
        // 获取上一天的日期
        System.out.println("执行统计任务");
        String day = DateUtil.parseDate2String(DateUtil.addDays(new Date(), -1));
        statisticsDailyService.generateStaDaily(day);
    }
}
