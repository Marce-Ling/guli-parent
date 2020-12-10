package com.atguigu.edu_service.api;

import com.atguigu.common_utils.R;
import com.atguigu.edu_service.entity.EduCourse;
import com.atguigu.edu_service.entity.EduTeacher;
import com.atguigu.edu_service.entity.web_vo.PageResult;
import com.atguigu.edu_service.service.EduCourseService;
import com.atguigu.edu_service.service.EduTeacherService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

/**
 * @Author Administrator
 * @CreateTime 2020-12-2
 * @Description
 */
@Api(description = "前台讲师管理")
@RestController
@RequestMapping("/eduService/teachermanage")
//@CrossOrigin
public class TeacherApiController {

    @Autowired
    private EduTeacherService teacherService;

    @Autowired
    private EduCourseService courseService;

    @Cacheable(value = "teacherWeb", key = "'selectPageList'")
    @ApiOperation(value = "获取讲师分页列表")
    @GetMapping("/pageList/{current}/{limit}")
    public R pageList(
            @ApiParam(name = "current", value = "当前页码", required = true)
            @PathVariable("current") Long current,
            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable("limit") Long limit) {
        Page<EduTeacher> page = new Page<>(current, limit);
        QueryWrapper<EduTeacher> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("gmt_create");
        teacherService.page(page, wrapper);
        PageResult pageResult = new PageResult(
                page.getCurrent(),
                page.getSize(),
                page.getPages(),
                page.getTotal(),
                page.getRecords(),
                page.hasNext(),
                page.hasPrevious());
        return R.ok()
                .data("pageInfo", pageResult);
    }

    @ApiOperation(value = "根据id获取讲师信息和其下所有的课程信息")
    @GetMapping("/getTeacherAndCourse/{id}")
    public R getTeacherAndCourse(@PathVariable("id") String id){
        QueryWrapper<EduCourse> courseWrapper = new QueryWrapper<>();
        courseWrapper.orderByDesc("gmt_modified")
                .eq("teacher_id", id);
        List<EduCourse> courses = courseService.list(courseWrapper);

        EduTeacher teacher = teacherService.getById(id);

        return R.ok()
                .data("teacher", teacher)
                .data("courseList", courses);
    }

}
