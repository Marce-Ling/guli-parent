package com.atguigu.edu_service.api;

import com.atguigu.common_utils.JwtUtils;
import com.atguigu.common_utils.R;
import com.atguigu.common_utils.vo.CourseInfoWebVO;
import com.atguigu.edu_service.entity.EduCourse;
import com.atguigu.edu_service.entity.vo.ChapterVO;
import com.atguigu.edu_service.entity.vo.CourseVO;
import com.atguigu.edu_service.entity.vo.SubjectVO;
import com.atguigu.edu_service.entity.web_vo.CourseDetailsWebVO;
import com.atguigu.edu_service.entity.web_vo.CourseQueryWeb;
import com.atguigu.edu_service.entity.web_vo.PageResult;
import com.atguigu.edu_service.feign.TOrderClient;
import com.atguigu.edu_service.service.EduChapterService;
import com.atguigu.edu_service.service.EduCourseService;
import com.atguigu.edu_service.service.EduSubjectService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @Author Administrator
 * @CreateTime 2020-12-3
 * @Description
 */
@Api(description = "前台课程管理")
@RestController
@RequestMapping("/eduService/coursemanage")
//@CrossOrigin
public class CourseApiController {
    @Autowired
    private EduCourseService courseService;

    @Autowired
    private EduSubjectService subjectService;

    @Autowired
    private EduChapterService chapterService;

    @Resource
    private TOrderClient orderClient;

    @Cacheable(value = "courseWeb", key = "'selectPageList'")
    @ApiOperation(value = "获取课程分页列表")
    @GetMapping("/pageList/{current}/{limit}")
    public R pageList(
            @ApiParam(name = "current", value = "当前页码", required = true)
            @PathVariable("current") Long current,
            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable("limit") Long limit,
            @ApiParam(name = "courseQuery", value = "查询条件", required = false)
                    CourseQueryWeb courseQuery) {
        Page<EduCourse> page = new Page<>(current, limit);
        courseService.pageListWeb(page, courseQuery);
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

    @ApiOperation(value = "根据课程id获取课程详情")
    @GetMapping("/getCourseDetails/{id}")
    public R getCourseDetails(@PathVariable("id") String id, HttpServletRequest request) {

        CourseDetailsWebVO courseDetails = courseService.getCourseDetails(id);

        List<ChapterVO> chapterDetails = chapterService.getChapterAndVideoInfo(id);

        // 获取用户id
        String memberId = JwtUtils.getMemberIdByJwtToken(request);

        // 查询当前用户是否已购买本课程
        boolean isBuyCourse = orderClient.getBuyCourse(id, memberId);

        return R.ok()
                .data("course", courseDetails)
                .data("chapterList", chapterDetails)
                .data("isBuyCourse", isBuyCourse);
    }

    @Cacheable(value = "subjectWeb", key = "'selectSubjectAll'")
    @ApiOperation(value = "获取所有的专业信息")
    @GetMapping("/getSubject")
    public R getSubject() {
        List<SubjectVO> list = subjectService.nestedList();
        return R.ok()
                .data("subjectList", list);
    }

    @ApiOperation(value = "根据课程id获取课程信息，用于远程调用")
    @GetMapping("/getCourseInfoWeb/{id}")
    public CourseInfoWebVO getCourseInfoWeb(@PathVariable("id") String id) {
        CourseVO course = courseService.getCoursePublishInfo(id);
        CourseInfoWebVO courseInfoWebVO = new CourseInfoWebVO();
        BeanUtils.copyProperties(course, courseInfoWebVO);
        return courseInfoWebVO;
    }
}
