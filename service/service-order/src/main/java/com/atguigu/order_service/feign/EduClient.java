package com.atguigu.order_service.feign;

import com.atguigu.common_utils.vo.CourseInfoWebVO;
import com.atguigu.order_service.feign.fallback.EduFileDegradeFeignClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @Author Administrator
 * @CreateTime 2020-12-4
 * @Description
 */
@Component
@FeignClient(name = "service-edu")
public interface EduClient {

    @GetMapping("/eduService/coursemanage/getCourseInfoWeb/{courseId}")
    CourseInfoWebVO getCourseInfoWeb(@PathVariable("courseId") String id);
}
