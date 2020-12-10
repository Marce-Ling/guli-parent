package com.atguigu.statistics_service.feign;

import com.atguigu.statistics_service.feign.impl.EduFileDegradeFeignClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @Author Administrator
 * @CreateTime 2020-12-7
 * @Description
 */
@Component
@FeignClient(name = "service-edu", fallback = EduFileDegradeFeignClient.class)
public interface EduClient {

    @GetMapping("/eduService/eduCourse/countNewCourseByDate/{day}")
    Integer countNewCourseByDate(@PathVariable("day") String date);
}
