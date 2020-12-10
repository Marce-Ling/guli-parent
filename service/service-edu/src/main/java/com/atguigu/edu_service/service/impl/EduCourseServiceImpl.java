package com.atguigu.edu_service.service.impl;

import com.atguigu.common_utils.ResultCode;
import com.atguigu.common_utils.constant.RedisConstant;
import com.atguigu.edu_service.entity.EduChapter;
import com.atguigu.edu_service.entity.EduCourse;
import com.atguigu.edu_service.entity.EduVideo;
import com.atguigu.edu_service.entity.vo.CourseQuery;
import com.atguigu.edu_service.entity.vo.CourseVO;
import com.atguigu.edu_service.entity.vo.EduCourseIntro;
import com.atguigu.edu_service.entity.web_vo.CourseDetailsWebVO;
import com.atguigu.edu_service.entity.web_vo.CourseQueryWeb;
import com.atguigu.edu_service.feign.VoDClient;
import com.atguigu.edu_service.mapper.EduCourseMapper;
import com.atguigu.edu_service.service.EduChapterService;
import com.atguigu.edu_service.service.EduCourseService;
import com.atguigu.edu_service.service.EduVideoService;
import com.atguigu.common_utils.exception.GuLiException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author marce
 * @since 2020-11-24
 */
@Service
public class EduCourseServiceImpl extends ServiceImpl<EduCourseMapper, EduCourse> implements EduCourseService {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private EduVideoService eduVideoService;

    @Autowired
    private EduChapterService chapterService;

    @Autowired
    private VoDClient voDClient;

    @Override
    @Transactional
    public String saveCourseInfo(EduCourse eduCourse) {

        eduCourse.setStatus(EduCourse.COURSE_DRAFT);

        // 添加到课程表
        baseMapper.insert(eduCourse);

        // 创建课程简介对象
        EduCourseIntro eduCourseIntro = null;
        if (!StringUtils.isEmpty(eduCourse.getDescription())) {
            eduCourseIntro = new EduCourseIntro();
            BeanUtils.copyProperties(eduCourse, eduCourseIntro);
        }

        // 添加到课程简介表
        baseMapper.addCourseIntro(eduCourseIntro);

        String cover = eduCourse.getCover();

        redisTemplate.boundSetOps(RedisConstant.EDU_TEACHER_PIC_DB_RESOURCES).add(cover);

        return eduCourse.getId();
    }

    @Override
    @Transactional
    public boolean updateById(EduCourse entity) {

        boolean update = super.updateById(entity);

        // 创建课程简介对象
        EduCourseIntro eduCourseIntro = null;
        if (!StringUtils.isEmpty(entity.getDescription())) {
            eduCourseIntro = new EduCourseIntro();
            BeanUtils.copyProperties(entity, eduCourseIntro);
        }

        baseMapper.updateCourseIntro(eduCourseIntro);

        String cover = entity.getCover();

        redisTemplate.boundSetOps(RedisConstant.EDU_TEACHER_PIC_DB_RESOURCES).add(cover);

        return update;
    }

    @Override
    @Transactional
    public EduCourse getOne(String id) {

        EduCourse eduCourse = baseMapper.selectById(id);
        if (eduCourse == null) {
            throw new GuLiException(ResultCode.ERROR, "数据不存在");
        }

        EduCourseIntro courseIntro = baseMapper.getCourseIntroById(id);
        if (courseIntro != null) {
            eduCourse.setDescription(courseIntro.getDescription());
        }
        return eduCourse;
    }

    @Override
    public CourseVO getCoursePublishInfo(String id) {

        // 根据id查询 课程表：title,lessonNum,price,cover 专业表：一、二级分类名称
        //      讲师表：name
        return baseMapper.getCoursePublishInfo(id);
    }

    @Override
    public List<CourseVO> getPageByCourseQuery(Long current, Long limit, CourseQuery courseQuery) {

        return baseMapper.getPageByCourseQuery(current, limit, courseQuery);
    }

    @Override
    public void deleteCourseRelevant(String courseId) {
        // 删除相关视频
        //  查询云端视频id
        QueryWrapper<EduVideo> videoIdWrapper = new QueryWrapper<>();
        videoIdWrapper.eq("course_id", courseId)
                .select("video_source_id");
        List<EduVideo> videoList = eduVideoService.list(videoIdWrapper);
        //      判断是否为空
        if (!StringUtils.isEmpty(videoList)) {
            // 云端视频id集合
            List<String> videoIdList = new ArrayList<>();
            for (EduVideo eduVideo : videoList) {
                if (!StringUtils.isEmpty(eduVideo)
                        && !StringUtils.isEmpty(eduVideo.getVideoSourceId())) {
                    videoIdList.add(eduVideo.getVideoSourceId());
                }
            }
            //  删除视频
            if (!StringUtils.isEmpty(videoIdList)) {
                voDClient.removeByIdList(videoIdList);
            }
        }
        // 删除相关课时
        QueryWrapper<EduVideo> videoWrapper = new QueryWrapper<>();
        videoWrapper.eq("course_id", courseId);
        boolean removeVideo = eduVideoService.remove(videoWrapper);
        // 删除相关章节
        QueryWrapper<EduChapter> chapterWrapper = new QueryWrapper<>();
        chapterWrapper.eq("course_id", courseId);
        boolean removeChapter = chapterService.remove(chapterWrapper);
        // 删除相关课程简介
        int removeIntro = baseMapper.deleteCourseIntroById(courseId);
        // 删除课程
        int removeCourse = baseMapper.deleteById(courseId);
        if (removeCourse <= 0) {
            throw new GuLiException(ResultCode.ERROR, "删除课程信息失败");
        }
    }

    @Override
    public void pageListWeb(Page<EduCourse> page, CourseQueryWeb courseQuery) {
        // 获取查询参数
        String subjectId = courseQuery.getSubjectId();
        String subjectParentId = courseQuery.getSubjectParentId();
        Boolean buyCountByDesc = courseQuery.getBuyCountByDesc();
        Boolean gmtModifiedByDesc = courseQuery.getGmtModifiedByDesc();
        Boolean priceByDesc = courseQuery.getPriceByDesc();

        // 判断参数，拼接sql
        QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(subjectId)) {
            wrapper.eq("subject_id", subjectId);
        }

        if (!StringUtils.isEmpty(subjectParentId)) {
            wrapper.eq("subject_parent_id", subjectParentId);
        }

        if (!StringUtils.isEmpty(priceByDesc)) {
            if (priceByDesc) {
                wrapper.orderByDesc("price");
            } else {
                wrapper.orderByAsc("price");
            }
        }

        if (!StringUtils.isEmpty(buyCountByDesc)) {
            if (buyCountByDesc) {
                wrapper.orderByDesc("buy_count");
            } else {
                wrapper.orderByAsc("buy_count");
            }
        }

        if (!StringUtils.isEmpty(gmtModifiedByDesc)) {
            if (gmtModifiedByDesc) {
                wrapper.orderByDesc("gmt_modified");
            } else {
                wrapper.orderByAsc("gmt_modified");
            }
        }

        if (StringUtils.isEmpty(priceByDesc)
                && StringUtils.isEmpty(gmtModifiedByDesc)
                && StringUtils.isEmpty(buyCountByDesc)) {
            wrapper.orderByDesc("gmt_create");
        }
        wrapper.eq("status", EduCourse.COURSE_NORMAL);

        baseMapper.selectPage(page, wrapper);
    }

    @Override
    public CourseDetailsWebVO getCourseDetails(String id) {

        // 更新浏览量
        this.updateViewCount(id);

        return baseMapper.selectCourseDetails(id);
    }

    @Override
    public void updateViewCount(String id) {
        // 每次点击查看视频，视频浏览量加1
        EduCourse course = baseMapper.selectById(id);
        course.setViewCount(course.getViewCount() + 1);
        baseMapper.updateById(course);
    }
}
