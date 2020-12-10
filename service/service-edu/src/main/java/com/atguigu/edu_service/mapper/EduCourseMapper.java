package com.atguigu.edu_service.mapper;

import com.atguigu.edu_service.entity.EduCourse;
import com.atguigu.edu_service.entity.vo.CourseQuery;
import com.atguigu.edu_service.entity.vo.CourseVO;
import com.atguigu.edu_service.entity.vo.EduCourseIntro;
import com.atguigu.edu_service.entity.web_vo.CourseDetailsWebVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 Mapper 接口
 * </p>
 *
 * @author marce
 * @since 2020-11-24
 */
public interface EduCourseMapper extends BaseMapper<EduCourse> {
    void addCourseIntro(EduCourseIntro eduCourseIntro);

    void updateCourseIntro(EduCourseIntro eduCourseIntro);

    EduCourseIntro getCourseIntroById(@Param("courseId") String courseId);

    CourseVO getCoursePublishInfo(@Param("courseId") String id);

    List<CourseVO> getPageByCourseQuery(
            @Param("current") Long current,
            @Param("limit") Long limit,
            @Param("courseQuery") CourseQuery courseQuery);

    Integer deleteCourseIntroById(String courseId);

    CourseDetailsWebVO selectCourseDetails(String id);
}
