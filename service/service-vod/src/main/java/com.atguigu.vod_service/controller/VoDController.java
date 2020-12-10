package com.atguigu.vod_service.controller;

import com.atguigu.common_utils.R;
import com.atguigu.vod_service.service.VoDService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @Author Administrator
 * @CreateTime 2020-11-28
 * @Description
 */
@Api(description = "阿里云视频点播微服务")
@RestController
@RequestMapping("/eduVod/video")
//@CrossOrigin
public class VoDController {

    @Autowired
    private VoDService videoService;

    @ApiOperation(value = "视频文件上传到阿里云VoD")
    @PostMapping("/uploadVideo")
    public R uploadVideo(
            @ApiParam(name = "file", value = "文件", required = true)
            MultipartFile file) throws Exception {

        String videoId = videoService.uploadVideo(file);
        return R.ok()
                .message("视频上传成功")
                .data("videoId", videoId);
    }

    @ApiOperation(value = "根据文件ID移除阿里云中的视频")
    @DeleteMapping("/removeById/{videoId}")
    public R removeById(
            @ApiParam(name = "videoId", value = "云端视频id", required = true)
            @PathVariable("videoId") String id) {
        if (null == id) {
            return R.error()
                    .message("参数错误");
        }
        videoService.removeById(id);
        return R.ok()
                .message("视频删除成功");
    }

    @ApiOperation(value = "根据多个文件ID移除阿里云中的视频")
    @DeleteMapping("/removeByIdList")
    public R removeByIdList(
            @ApiParam(name = "videoIdList", value = "多个云端视频id", required = true)
            @RequestParam("videoIdList") List<String> videoIdList) {
        videoService.removeByIdList(videoIdList);
        return R.ok()
                .message("视频删除成功");
    }

    @ApiOperation(value = "根据多个文件ID移除阿里云中的视频")
    @GetMapping("/getVideoPlay/{id}")
    public R getVideoPlay(@PathVariable("id") String videoSourceId){
        String playAuth = videoService.getVideoPlayAuth(videoSourceId);
        return R.ok()
                .data("playAuth", playAuth)
                .message("获取凭证成功");
    }
}
