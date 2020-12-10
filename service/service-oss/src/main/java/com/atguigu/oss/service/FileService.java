package com.atguigu.oss.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @Author Administrator
 * @CreateTime 2020-11-21
 * @Description
 */
public interface FileService {

    /**
     * 文件上传至阿里云
     * @param file
     */
    String upload(MultipartFile file);

}
