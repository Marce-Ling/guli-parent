package com.atguigu.edu_service.service.impl;

import com.atguigu.edu_service.entity.EduVideo;
import com.atguigu.edu_service.feign.VoDClient;
import com.atguigu.edu_service.mapper.EduVideoMapper;
import com.atguigu.edu_service.service.EduVideoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程视频 服务实现类
 * </p>
 *
 * @author marce
 * @since 2020-11-25
 */
@Service
public class EduVideoServiceImpl extends ServiceImpl<EduVideoMapper, EduVideo> implements EduVideoService {

    @Autowired
    private VoDClient voDClient;

    @Override
    public boolean deleteById(String id) {

        // 删除阿里云中的相关视频
        //     查询videoId
        EduVideo eduVideo = baseMapper.selectById(id);
        String videoId = eduVideo.getVideoSourceId();
        if (!StringUtils.isEmpty(videoId)) {
            // 根据视频id删除视频
            voDClient.removeById(videoId);
        }

        // 删除课时
        Integer delete = baseMapper.deleteById(id);

        return delete != null && delete > 0;
    }

    @Override
    public boolean deleteByChapterId(String chapterId) {

        // 删除阿里云中的相关视频
        //     查询videoId
        QueryWrapper<EduVideo> wrapper = new QueryWrapper<>();
        wrapper.eq("chapter_id", chapterId);
        List<EduVideo> eduVideos = baseMapper.selectList(wrapper);
        // 封装videoId集合
        List<String> videoIdList = null;
        for (EduVideo eduVideo : eduVideos) {
            if (null == videoIdList) {
                videoIdList = new ArrayList<>();
            }
            if (!StringUtils.isEmpty(eduVideo.getVideoSourceId())) {
                videoIdList.add(eduVideo.getVideoSourceId());
            }
        }

        // 删除云端视频资源
        voDClient.removeByIdList(videoIdList);

        // 删除课时
        Integer delete = baseMapper.delete(wrapper);

        return delete != null && delete > 0;
    }
}
