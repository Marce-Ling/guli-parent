package com.atguigu.edu_service.service;

import com.atguigu.edu_service.entity.EduCourse;
import com.atguigu.edu_service.entity.vo.CourseQuery;
import com.atguigu.edu_service.entity.vo.CourseVO;
import com.atguigu.edu_service.entity.web_vo.CourseDetailsWebVO;
import com.atguigu.edu_service.entity.web_vo.CourseQueryWeb;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author marce
 * @since 2020-11-24
 */
public interface EduCourseService extends IService<EduCourse> {

    String saveCourseInfo(EduCourse eduCourse);

    EduCourse getOne(String id);

    CourseVO getCoursePublishInfo(String id);

    List<CourseVO> getPageByCourseQuery(Long current, Long limit, CourseQuery courseQuery);

    void deleteCourseRelevant(String courseId);

    void pageListWeb(Page<EduCourse> page, CourseQueryWeb courseQuery);

    CourseDetailsWebVO getCourseDetails(String id);

    void updateViewCount(String id);
}
