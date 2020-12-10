package com.atguigu.vod_service.service.impl;

import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadStreamRequest;
import com.aliyun.vod.upload.resp.UploadStreamResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.vod.model.v20170321.DeleteVideoResponse;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthResponse;
import com.atguigu.common_utils.exception.GuLiException;
import com.atguigu.vod_service.service.VoDService;
import com.atguigu.vod_service.utils.AliyunVodSDKUtils;
import com.atguigu.vod_service.utils.ConstantPropertiesUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @Author Administrator
 * @CreateTime 2020-11-28
 * @Description
 */
@Service
public class VoDServiceImpl implements VoDService {
    @Override
    public String uploadVideo(MultipartFile file) {
        try {
            InputStream inputStream = file.getInputStream();
            // 文件原始名称（带后缀）
            String originalFilename = file.getOriginalFilename();
            // 去文件后缀作为标题
            String title = originalFilename.substring(0, originalFilename.lastIndexOf("."));

            UploadStreamRequest request = new UploadStreamRequest(
                    ConstantPropertiesUtil.ACCESS_KEY_ID,
                    ConstantPropertiesUtil.ACCESS_KEY_SECRET,
                    title, originalFilename, inputStream);

            UploadVideoImpl uploader = new UploadVideoImpl();
            UploadStreamResponse response = uploader.uploadStream(request);

            //如果设置回调URL无效，不影响视频上传，可以返回VideoId同时会返回错误码。
            // 其他情况上传失败时，VideoId为空，此时需要根据返回错误码分析具体错误原因
            String videoId = response.getVideoId();
            if (!response.isSuccess()) {
                String errorMessage = "阿里云上传错误：" + "code：" + response.getCode() + ", message：" + response.getMessage();
//                log.warn(errorMessage);
                if (StringUtils.isEmpty(videoId)) {
                    throw new GuLiException(20001, errorMessage);
                }
            }

            return videoId;
        } catch (IOException e) {
            throw new GuLiException(20001, "guli vod 服务上传失败");
        }
    }

    @Override
    public void removeById(String id) {
        try {
            DefaultAcsClient client = AliyunVodSDKUtils.initVodClient(
                    ConstantPropertiesUtil.ACCESS_KEY_ID,
                    ConstantPropertiesUtil.ACCESS_KEY_SECRET);
            DeleteVideoResponse response = AliyunVodSDKUtils.deleteVideo(client, id);
        } catch (Exception e) {
            throw new GuLiException(20001, "视频删除失败");
        }
    }

    @Override
    public void removeByIdList(List<String> videoIdList) {
        try {
            DefaultAcsClient client = AliyunVodSDKUtils.initVodClient(
                    ConstantPropertiesUtil.ACCESS_KEY_ID,
                    ConstantPropertiesUtil.ACCESS_KEY_SECRET);
            DeleteVideoResponse response = AliyunVodSDKUtils.deleteVideo(client,
                    StringUtils.toStringArray(videoIdList));
        } catch (Exception e) {
            throw new GuLiException(20001, "视频删除失败");
        }
    }

    @Override
    public String getVideoPlayAuth(String videoSourceId) {
        try {
            DefaultAcsClient client = AliyunVodSDKUtils.initVodClient(
                    ConstantPropertiesUtil.ACCESS_KEY_ID,
                    ConstantPropertiesUtil.ACCESS_KEY_SECRET);
            GetVideoPlayAuthResponse response = new GetVideoPlayAuthResponse();
            response = AliyunVodSDKUtils.getVideoPlayAuth(client,videoSourceId);
            //播放凭证
            String playAuth = response.getPlayAuth();
//            System.out.print("PlayAuth = " + playAuth + "\n");
            //VideoMeta信息
//            System.out.print("VideoMeta.Title = " + response.getVideoMeta().getTitle() + "\n");
            // 返回播放凭证
            return playAuth;
        } catch (Exception e) {
            System.out.print("ErrorMessage = " + e.getLocalizedMessage());
            throw new GuLiException(20001, "获取视频播放凭证失败");
        }
    }
}
