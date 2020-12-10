package com.atguigu.edu_service.feign;

import com.atguigu.common_utils.vo.UcenterMemberWebVO;
import com.atguigu.edu_service.feign.fallback.UCenterFileDegradeFeignClient;
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
@FeignClient(name = "service-ucenter", fallback = UCenterFileDegradeFeignClient.class)
public interface UCenterClient {
    @GetMapping("/ucenterService/apiMember/getMemberInfo/{id}")
    UcenterMemberWebVO getMemberInfo(@PathVariable("id") String id);
}
