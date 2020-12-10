package com.atguigu.statistics_service.feign.impl;

import com.atguigu.statistics_service.feign.UCenterClient;
import org.springframework.stereotype.Component;

/**
 * @Author Administrator
 * @CreateTime 2020-12-4
 * @Description
 */
@Component
public class UCenterFileDegradeFeignClient implements UCenterClient {
    @Override
    public Integer countRegisterByDate(String date) {
        return null;
    }
}
