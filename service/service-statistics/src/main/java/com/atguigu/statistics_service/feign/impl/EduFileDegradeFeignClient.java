package com.atguigu.statistics_service.feign.impl;

import com.atguigu.statistics_service.feign.EduClient;
import org.springframework.stereotype.Component;

/**
 * @Author Administrator
 * @CreateTime 2020-12-4
 * @Description
 */
@Component
public class EduFileDegradeFeignClient implements EduClient {
    @Override
    public Integer countNewCourseByDate(String date) {
        return null;
    }
}
