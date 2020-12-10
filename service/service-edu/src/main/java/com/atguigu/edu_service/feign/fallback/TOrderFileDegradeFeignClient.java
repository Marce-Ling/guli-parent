package com.atguigu.edu_service.feign.fallback;

import com.atguigu.common_utils.vo.UcenterMemberWebVO;
import com.atguigu.edu_service.feign.TOrderClient;
import com.atguigu.edu_service.feign.UCenterClient;
import org.springframework.stereotype.Component;

/**
 * @Author Administrator
 * @CreateTime 2020-12-4
 * @Description
 */
@Component
public class TOrderFileDegradeFeignClient implements TOrderClient {

    @Override
    public boolean getBuyCourse(String courseId, String memberId) {
        return false;
    }
}
