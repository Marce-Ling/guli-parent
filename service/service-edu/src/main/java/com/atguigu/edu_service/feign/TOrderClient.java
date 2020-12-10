package com.atguigu.edu_service.feign;

import com.atguigu.edu_service.feign.fallback.TOrderFileDegradeFeignClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @Author Administrator
 * @CreateTime 2020-12-6
 * @Description
 */
@Component
@FeignClient(name = "service-order", fallback = TOrderFileDegradeFeignClient.class)
public interface TOrderClient {

    @GetMapping("/orderservice/ordermanage/getOrderInfoByNo/{courseId}/{memberId}")
    public boolean getBuyCourse(
            @PathVariable("courseId") String courseId,
            @PathVariable("memberId") String memberId);
}
