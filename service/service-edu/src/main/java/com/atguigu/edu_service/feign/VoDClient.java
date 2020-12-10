package com.atguigu.edu_service.feign;

import com.atguigu.common_utils.R;
import com.atguigu.edu_service.feign.fallback.VodFileDegradeFeignClient;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Author Administrator
 * @CreateTime 2020-11-29
 * @Description 远程调用eduVoD微服务的接口
 */
@Component
@FeignClient(name = "service-vod", fallback = VodFileDegradeFeignClient.class)
public interface VoDClient {

    @ApiOperation(value = "根据文件ID移除阿里云中的视频")
    @DeleteMapping("/eduVod/video/removeById/{id}")
    R removeById(
            @ApiParam(name = "videoId", value = "云端视频id", required = true)
            @PathVariable("id") String id);

    @ApiOperation(value = "根据多个文件ID移除阿里云中的视频")
    @DeleteMapping("/eduVod/video/removeByIdList")
    R removeByIdList(
            @ApiParam(name = "videoIdList", value = "多个云端视频id", required = true)
            @RequestParam("videoIdList") List<String> videoIdList);
}
