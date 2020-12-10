package com.atguigu.edu_service.service;

import com.atguigu.edu_service.entity.EduVideo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 课程视频 服务类
 * </p>
 *
 * @author marce
 * @since 2020-11-25
 */
public interface EduVideoService extends IService<EduVideo> {

    boolean deleteById(String id);

    boolean deleteByChapterId(String chapterId);
}
