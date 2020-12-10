package com.atguigu.vod_service.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @Author Administrator
 * @CreateTime 2020-11-28
 * @Description
 */
public interface VoDService {
    String uploadVideo(MultipartFile file);

    void removeById(String id);

    void removeByIdList(List<String> videoIdList);

    String getVideoPlayAuth(String videoSourceId);
}
