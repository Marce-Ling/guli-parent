package com.atguigu.statistics_service.feign;

import com.atguigu.statistics_service.feign.impl.UCenterFileDegradeFeignClient;
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
@FeignClient(name = "service-ucenter", fallback = UCenterFileDegradeFeignClient.class)
public interface UCenterClient {

    @GetMapping("/ucenterService/backmember/countRegisterByDate/{day}")
    Integer countRegisterByDate(@PathVariable("day") String date);
}
