package com.atguigu.edu_service.service.impl;

import com.atguigu.edu_service.entity.EduChapter;
import com.atguigu.edu_service.entity.EduVideo;
import com.atguigu.edu_service.entity.vo.ChapterVO;
import com.atguigu.edu_service.entity.vo.VideoVO;
import com.atguigu.edu_service.mapper.EduChapterMapper;
import com.atguigu.edu_service.service.EduChapterService;
import com.atguigu.edu_service.service.EduVideoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author marce
 * @since 2020-11-25
 */
@Service
public class EduChapterServiceImpl extends ServiceImpl<EduChapterMapper, EduChapter> implements EduChapterService {

    @Autowired
    private EduVideoService eduVideoService;

    @Override
    public List<ChapterVO> getChapterAndVideoInfo(String courseId) {
        // 封装视图数据的集合
        List<ChapterVO> chapterAndVideoList = new ArrayList<>();

        // 根据课程id查询章节
        QueryWrapper<EduChapter> chapterWrapper = new QueryWrapper<>();
        chapterWrapper.eq("course_id", courseId).orderByAsc("sort");
        List<EduChapter> chapterList = baseMapper.selectList(chapterWrapper);

        if (chapterList == null) {
            return null;
        }

        // 根据课程id查询课时
        QueryWrapper<EduVideo> videoWrapper = new QueryWrapper<>();
        videoWrapper.eq("course_id", courseId).orderByAsc("sort");
        List<EduVideo> videoList = eduVideoService.list(videoWrapper);

        // 遍历章节集合，封装章节信息
        for (EduChapter eduChapter : chapterList) {
            // 章节视图数据，包含课时视图数据集合
            ChapterVO chapterVO = new ChapterVO();
            BeanUtils.copyProperties(eduChapter, chapterVO);
            // 封装课时视图数据的集合
            List<VideoVO> children = new ArrayList<>();
            // 遍历课时集合，封装课时信息
            for (EduVideo eduVideo : videoList) {
                // 判断课时属于哪个章节
                if (eduChapter.getId().equals(eduVideo.getChapterId())) {
                    // 课时视图数据
                    VideoVO videoVO = new VideoVO();
                    BeanUtils.copyProperties(eduVideo, videoVO);
                    children.add(videoVO);
                }
            }
            // 往章节视图数据中添加课时视图数据
            chapterVO.setChildren(children);
            // 将封装好的章节视图数据添加到返回数据集合中
            chapterAndVideoList.add(chapterVO);
        }

        return chapterAndVideoList;
    }

    @Override
    public void updateChapterAndVideoSort(String courseId, List<ChapterVO> chapterVOList) {

        // 根据课程id查询章节
        QueryWrapper<EduChapter> chapterWrapper = new QueryWrapper<>();
        chapterWrapper.eq("course_id", courseId).orderByAsc("sort");
        List<EduChapter> chapterList = baseMapper.selectList(chapterWrapper);

        // 根据课程id查询课时
        QueryWrapper<EduVideo> videoWrapper = new QueryWrapper<>();
        videoWrapper.eq("course_id", courseId).orderByAsc("sort");
        List<EduVideo> videoList = eduVideoService.list(videoWrapper);

        // 更新章节排序
        for (ChapterVO chapterVO : chapterVOList) {
            List<VideoVO> children = chapterVO.getChildren();
            for (ListIterator<EduChapter> chapterIterator = chapterList.listIterator();
                 chapterIterator.hasNext();) {
                EduChapter eduChapter = chapterIterator.next();
                if (eduChapter.getId().equals(chapterVO.getId())) {
                    // 排序没有变化，移除出更新列表
                    if (eduChapter.getSort().equals(chapterVO.getSort())) {
                        chapterIterator.remove();
                    } else {
                        eduChapter.setSort(chapterVO.getSort());
                    }
                }
            }

            // 更新课时排序
            for (VideoVO child : children) {
                for (ListIterator<EduVideo> videoIterator = videoList.listIterator();
                     videoIterator.hasNext();) {
                    EduVideo eduVideo = videoIterator.next();
                    if (eduVideo.getId().equals(child.getId())) {
                        // 排序没有变化，移除出更新列表
                        if (eduVideo.getSort().equals(child.getSort())) {
                            videoIterator.remove();
                        } else {
                            eduVideo.setSort(child.getSort());
                        }

                    }
                }
            }
        }

        // 更新
        for (EduChapter eduChapter : chapterList) {
            int update = baseMapper.updateById(eduChapter);
        }

        if (videoList != null && videoList.size() != 0) {
            eduVideoService.updateBatchById(videoList);
        }

    }
}
