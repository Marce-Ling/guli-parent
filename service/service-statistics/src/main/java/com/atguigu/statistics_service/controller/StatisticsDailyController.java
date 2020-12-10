package com.atguigu.statistics_service.controller;


import com.atguigu.common_utils.R;
import com.atguigu.statistics_service.service.StatisticsDailyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 网站统计日数据 前端控制器
 * </p>
 *
 * @author marce
 * @since 2020-12-07
 */
@Api(description = "网站数据统计管理")
@RestController
@RequestMapping("/staservice/stadaily")
//@CrossOrigin
public class StatisticsDailyController {
    @Autowired
    private StatisticsDailyService staDailyService;

    @ApiOperation(value = "统计网站日数据")
    @GetMapping("/generateStaDaily/{day}")
    public R generateStaDaily(@PathVariable("day") String day) {
        staDailyService.generateStaDaily(day);
        return R.ok();
    }

    @ApiOperation(value = "查询统计数据")
    @GetMapping("/queryStaDailies/{type}/{beginDay}/{endDay}")
    public R queryStaDailies(@PathVariable("type") String type,
                             @PathVariable("beginDay") String beginDay,
                             @PathVariable("endDay") String endDay) {
        Map<String, Object> data = staDailyService.queryStaDailies(type, beginDay, endDay);
        return R.ok()
                .data(data);

    }
}

