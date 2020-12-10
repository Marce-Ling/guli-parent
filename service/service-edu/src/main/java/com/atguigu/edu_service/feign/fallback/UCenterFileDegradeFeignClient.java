package com.atguigu.edu_service.feign.fallback;

import com.atguigu.common_utils.vo.UcenterMemberWebVO;
import com.atguigu.edu_service.feign.UCenterClient;
import org.springframework.stereotype.Component;

/**
 * @Author Administrator
 * @CreateTime 2020-12-4
 * @Description
 */
@Component
public class UCenterFileDegradeFeignClient implements UCenterClient {
    @Override
    public UcenterMemberWebVO getMemberInfo(String id) {
        return null;
    }
}
