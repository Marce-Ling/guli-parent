package com.atguigu.order_service.feign.fallback;

import com.atguigu.common_utils.vo.CourseInfoWebVO;
import com.atguigu.order_service.feign.EduClient;
import org.springframework.stereotype.Component;

/**
 * @Author Administrator
 * @CreateTime 2020-12-4
 * @Description
 */
@Component
public class EduFileDegradeFeignClient implements EduClient {

    @Override
    public CourseInfoWebVO getCourseInfoWeb(String id) {
        return null;
    }
}
