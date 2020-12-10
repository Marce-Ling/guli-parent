package com.atguigu.edu_service.controller;


import com.atguigu.common_utils.R;
import com.atguigu.edu_service.entity.EduCourse;
import com.atguigu.edu_service.entity.vo.CourseQuery;
import com.atguigu.edu_service.entity.vo.CourseVO;
import com.atguigu.edu_service.service.EduCourseService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author marce
 * @since 2020-11-24
 */
@Api(description = "课程发布")
@RestController
@RequestMapping("/eduService/eduCourse")
//@CrossOrigin
public class EduCourseController {

    @Autowired
    private EduCourseService eduCourseService;

    @ApiOperation(value = "添加课程基本信息")
    @PostMapping("/addCourseInfo")
    public R addCourseInfo(@RequestBody EduCourse eduCourse){

        String courseId = eduCourseService.saveCourseInfo(eduCourse);

        if (StringUtils.isEmpty(courseId)) {
            return R.error()
                    .message("课程保存失败");
        }

        return R.ok()
                .data("id", courseId);
    }

    @ApiOperation(value = "修改课程基本信息")
    @PutMapping("/updateCourseInfo")
    public R updateCourseInfo(@RequestBody EduCourse eduCourse){

        boolean update = eduCourseService.updateById(eduCourse);
        if (update) {
            return R.ok();
        }else{
            return R.error();
        }
    }

    @ApiOperation(value = "根据课程id获取课程基本信息")
    @GetMapping("/getById/{id}")
    public R getById(@PathVariable("id") String id){

        EduCourse courseInfo = eduCourseService.getOne(id);

        return R.ok()
                .data("data", courseInfo);
    }

    @ApiOperation(value = "更新课程总课时")
    @PostMapping("/updateLessonNum")
    public R updateLessonNum (@RequestParam("id") String id, @RequestParam("lessonNum") Integer lessonNum) {
        EduCourse eduCourse = eduCourseService.getById(id);
        boolean update = true;
        if (!eduCourse.getLessonNum().equals(lessonNum)) {
            eduCourse.setLessonNum(lessonNum);
            UpdateWrapper<EduCourse> wrapper = new UpdateWrapper<>();
            wrapper.eq("id", id);
            update = eduCourseService.update(eduCourse, wrapper);
        }
        if (update) {
            return R.ok();
        }else{
            return R.error()
                    .message("课程总课时更新失败");
        }
    }

    @ApiOperation(value = "根据id获取待发布课程信息")
    @GetMapping("/getCoursePublishInfo/{id}")
    public R getCoursePublishInfo(@PathVariable("id") String id){
        CourseVO coursePublishInfo = eduCourseService.getCoursePublishInfo(id);
        return R.ok()
                .data("data", coursePublishInfo);
    }

    @ApiOperation(value = "发布课程，更新课程状态")
    @PutMapping("/publishCourse/{id}")
    public R publishCourse(@PathVariable("id") String id){
        EduCourse eduCourse = eduCourseService.getById(id);
        if (eduCourse.getStatus().equals(EduCourse.COURSE_DRAFT)) {
            eduCourse.setStatus(EduCourse.COURSE_NORMAL);
            eduCourseService.updateById(eduCourse);
        }
        return R.ok();
    }

    @ApiOperation(value = "根据查询条件获取课程列表")
    @GetMapping("/getPage/{current}/{limit}")
    public R getPage(
            @ApiParam(name = "current", value = "当前页", required = true)
            @PathVariable("current") Long current,
            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable("limit") Long limit,
            @ApiParam(name = "teacherQuery", value = "查询条件")
            CourseQuery courseQuery) {

        QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(courseQuery.getTitle())){
            wrapper.like("title", courseQuery.getTitle());
        }

        if (!StringUtils.isEmpty(courseQuery.getSubjectId())){
            wrapper.eq("subject_id", courseQuery.getSubjectId());
        }

        if (!StringUtils.isEmpty(courseQuery.getSubjectParentId())) {
            wrapper.eq("subject_parent_id", courseQuery.getSubjectParentId());
        }

        if (!StringUtils.isEmpty(courseQuery.getTeacherId())) {
            wrapper.eq("teacher_id", courseQuery.getTeacherId());
        }

        int count = eduCourseService.count(wrapper);

        List<CourseVO> list = eduCourseService.getPageByCourseQuery((current - 1) * limit , limit, courseQuery);

        return R.ok()
                .data("data", list)
                .data("total", count);
    }

    @ApiOperation(value = "根据课程id删除课程相关信息")
    @DeleteMapping("/delCourse/{id}")
    public R delCourse(@PathVariable("id") String courseId){

        eduCourseService.deleteCourseRelevant(courseId);

        return R.ok();
    }

    @ApiOperation(value = "统计某日期下的新增课程数，远程调用")
    @GetMapping("/countNewCourseByDate/{day}")
    public Integer countNewCourseByDate(@PathVariable("day") String date) {
        QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();
        wrapper.likeRight("gmt_create", date);
        return eduCourseService.count(wrapper);
    }

}

