package com.atguigu.statistics_service.service;

import com.atguigu.statistics_service.entity.StatisticsDaily;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 网站统计日数据 服务类
 * </p>
 *
 * @author marce
 * @since 2020-12-07
 */
public interface StatisticsDailyService extends IService<StatisticsDaily> {

    void generateStaDaily(String day);

    Map<String, Object> queryStaDailies(String type, String beginDay, String endDay);
}
