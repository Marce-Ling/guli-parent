package com.atguigu.oss.service.impl;

import com.atguigu.oss.constant.ConstantPropertiesUtil;
import com.atguigu.oss.service.FileService;
import com.atguigu.oss.utils.OssUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


/**
 * @Author Administrator
 * @CreateTime 2020-11-21
 * @Description
 */
@Service
public class FileServiceImpl implements FileService {

    @Override
    public String upload(MultipartFile file) {

        String uploadUrl = OssUtil.upload2Aliyun(file);

        return uploadUrl;
    }
}
