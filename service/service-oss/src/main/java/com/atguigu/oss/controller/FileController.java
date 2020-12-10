package com.atguigu.oss.controller;

import com.atguigu.common_utils.R;
import com.atguigu.common_utils.constant.RedisConstant;
import com.atguigu.oss.service.FileService;
import com.atguigu.oss.utils.OssUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Set;

/**
 * @Author Administrator
 * @CreateTime 2020-11-21
 * @Description
 */
@Api(description = "阿里云文件管理")
@RestController
@RequestMapping("/oss")
//@CrossOrigin
public class FileController {

    @Autowired
    private FileService fileService;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @ApiOperation(value = "文件上传")
    @PostMapping("/upload")
    public R upload(@ApiParam(name = "file", value = "文件", required = true)
                    @RequestParam("file") MultipartFile file){
        String uploadUrl = fileService.upload(file);

        // 使用Redis记录上传成功的文件名称
        redisTemplate.boundSetOps(RedisConstant.EDU_TEACHER_PIC_RESOURCES)
                .add(uploadUrl);

        return R.ok()
                .data("url", uploadUrl)
                .message("文件上传成功");
    }


    @Scheduled(cron = "* 30 * * * ?")
    public void cleanRedundantAvatar(){
        Set<Object> redundantAvatar = redisTemplate
                .boundSetOps(RedisConstant.EDU_TEACHER_PIC_RESOURCES)
                .diff(RedisConstant.EDU_TEACHER_PIC_DB_RESOURCES);

        assert redundantAvatar != null;
        for (Object o : redundantAvatar) {
            String fileUrl = (String) o;
            OssUtil.delete2Aliyun(fileUrl);
        }
    }

}
