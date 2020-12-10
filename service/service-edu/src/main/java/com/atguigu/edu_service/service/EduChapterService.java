package com.atguigu.edu_service.service;

import com.atguigu.edu_service.entity.EduChapter;
import com.atguigu.edu_service.entity.vo.ChapterVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author marce
 * @since 2020-11-25
 */
public interface EduChapterService extends IService<EduChapter> {

    List<ChapterVO> getChapterAndVideoInfo(String courseId);

    void updateChapterAndVideoSort(String courseId, List<ChapterVO> chapterVOList);
}
