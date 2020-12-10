package com.atguigu.edu_service.feign.fallback;

import com.atguigu.common_utils.R;
import com.atguigu.edu_service.feign.VoDClient;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author Administrator
 * @CreateTime 2020-11-29
 * @Description
 */
@Component
public class VodFileDegradeFeignClient implements VoDClient {
    @Override
    public R removeById(String id) {
        return R.error().message("time out");
    }

    @Override
    public R removeByIdList(List<String> videoIdList) {
        return R.error().message("time out");
    }
}
