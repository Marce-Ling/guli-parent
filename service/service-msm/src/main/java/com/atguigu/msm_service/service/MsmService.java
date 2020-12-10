package com.atguigu.msm_service.service;

import java.util.Map;

/**
 * @Author Administrator
 * @CreateTime 2020-12-1
 * @Description
 */
public interface MsmService {
    boolean send(String phoneNumber, String templateCode, Map<String, Object> param);
}
