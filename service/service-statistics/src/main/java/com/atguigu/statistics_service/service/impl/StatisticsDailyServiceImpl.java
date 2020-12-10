package com.atguigu.statistics_service.service.impl;

import com.atguigu.statistics_service.entity.StatisticsDaily;
import com.atguigu.statistics_service.feign.EduClient;
import com.atguigu.statistics_service.feign.UCenterClient;
import com.atguigu.statistics_service.mapper.StatisticsDailyMapper;
import com.atguigu.statistics_service.service.StatisticsDailyService;
import com.atguigu.statistics_service.utils.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;

/**
 * <p>
 * 网站统计日数据 服务实现类
 * </p>
 *
 * @author marce
 * @since 2020-12-07
 */
@Service
public class StatisticsDailyServiceImpl extends ServiceImpl<StatisticsDailyMapper, StatisticsDaily> implements StatisticsDailyService {

    @Resource
    private UCenterClient uCenterClient;

    @Resource
    private EduClient eduClient;

    @Override
    public void generateStaDaily(String day) {
        // 删除已存在的统计对象
        QueryWrapper<StatisticsDaily> staDailyWrapper = new QueryWrapper<>();
        staDailyWrapper.eq("date_calculated", day);
        baseMapper.delete(staDailyWrapper);
        // 获取统计信息
        Integer registerNum = uCenterClient.countRegisterByDate(day);
        Integer courseNum = eduClient.countNewCourseByDate(day);
        int videoViewNum = RandomUtils.nextInt(100, 200);
        int loginNum = RandomUtils.nextInt(100, 200);
        // 创建统计对象
        StatisticsDaily statisticsDaily = new StatisticsDaily();
        statisticsDaily.setCourseNum(courseNum)
                .setRegisterNum(registerNum)
                .setVideoViewNum(videoViewNum)
                .setLoginNum(loginNum)
                .setDateCalculated(day);
        // 存储到数据库
        baseMapper.insert(statisticsDaily);
    }

    @Override
    public Map<String, Object> queryStaDailies(String type, String beginDay, String endDay){
        // 查询统计数据
        QueryWrapper<StatisticsDaily> wrapper = new QueryWrapper<>();
        wrapper.orderByAsc("date_calculated")
                .between("date_calculated", beginDay, endDay)
                .select(type, "date_calculated");
        List<StatisticsDaily> staDailiesList = baseMapper.selectList(wrapper);

        // 封装返回数据的Map集合
        Map<String, Object> map = new HashMap<>();
        // 封装Y轴数据的List集合，统计类型
        List<Integer> yData = new ArrayList<>();
        // 封装X轴数据的List集合，统计日期
        List<String> xData = new ArrayList<>();

        map.put("xData", xData);
        map.put("yData", yData);

        // 遍历查询出来的数据，进行封装
        for (StatisticsDaily staDaily : staDailiesList) {
            xData.add(staDaily.getDateCalculated());
            switch (type) {
                case "register_num":
                    yData.add(staDaily.getRegisterNum());
                    break;
                case "login_num":
                    yData.add(staDaily.getLoginNum());
                    break;
                case "video_view_num":
                    yData.add(staDaily.getVideoViewNum());
                    break;
                case "course_num":
                    yData.add(staDaily.getCourseNum());
                    break;
            }
        }

        return map;
    }
}
