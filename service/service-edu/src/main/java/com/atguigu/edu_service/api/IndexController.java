package com.atguigu.edu_service.api;

import com.atguigu.common_utils.R;
import com.atguigu.edu_service.entity.EduCourse;
import com.atguigu.edu_service.entity.EduTeacher;
import com.atguigu.edu_service.service.EduCourseService;
import com.atguigu.edu_service.service.EduTeacherService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author Administrator
 * @CreateTime 2020-11-30
 * @Description
 */
@Api(description = "首页讲师和课程信息展示管理")
@RestController
@RequestMapping("/eduService/indexInfo")
//@CrossOrigin
public class IndexController {

    @Autowired
    private EduCourseService courseService;

    @Autowired
    private EduTeacherService teacherService;

    @Cacheable(value = "indexInfo", key = "'selectIndexList'")
    @ApiOperation(value = "展示8门课程和4个讲师信息")
    @GetMapping("/getTeacherAndCourse")
    public R getTeacherAndCourse() {
        QueryWrapper<EduCourse> courseWrapper = new QueryWrapper<>();
        courseWrapper.orderByDesc("gmt_create")
                .last("LIMIT 8");
        List<EduCourse> courses = courseService.list(courseWrapper);

        QueryWrapper<EduTeacher> teacherWrapper = new QueryWrapper<>();
        teacherWrapper.orderByDesc("gmt_create")
                .last("LIMIT 4");
        List<EduTeacher> teachers = teacherService.list(teacherWrapper);

        return R.ok()
                .data("teacherList", teachers)
                .data("courseList", courses);
    }

}
