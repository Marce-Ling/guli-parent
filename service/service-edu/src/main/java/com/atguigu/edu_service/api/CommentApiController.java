package com.atguigu.edu_service.api;

import com.atguigu.common_utils.JwtUtils;
import com.atguigu.common_utils.R;
import com.atguigu.common_utils.ResultCode;
import com.atguigu.common_utils.exception.GuLiException;
import com.atguigu.common_utils.vo.UcenterMemberWebVO;
import com.atguigu.edu_service.entity.EduComment;
import com.atguigu.edu_service.entity.web_vo.PageResult;
import com.atguigu.edu_service.feign.UCenterClient;
import com.atguigu.edu_service.service.EduCommentService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author Administrator
 * @CreateTime 2020-12-4
 * @Description
 */
@Api(description = "前台评论管理")
@RestController
@RequestMapping("/eduService/commentmanage")
//@CrossOrigin
public class CommentApiController {

    @Autowired
    private EduCommentService commentService;

    @Autowired
    private UCenterClient ucenterClient;

    @ApiOperation(value = "根据课程id获取评论列表信息")
    @GetMapping("/pageList/{current}/{limit}/{courseId}")
    public R pageList(
            @ApiParam(name = "current", value = "当前页码", required = true)
            @PathVariable("current") Long current,
            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable("limit") Long limit,
            @ApiParam(name = "courseId", value = "课程id", required = true)
            @PathVariable("courseId") String courseId) {

        Page<EduComment> page = new Page<>(current, limit);
        QueryWrapper<EduComment> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("gmt_modified")
                .eq("course_id", courseId);
        commentService.page(page, wrapper);
        PageResult pageResult = new PageResult(
                page.getCurrent(),
                page.getSize(),
                page.getPages(),
                page.getTotal(),
                page.getRecords(),
                page.hasNext(),
                page.hasPrevious());
        return R.ok()
                .data("commentPageInfo", pageResult);
    }

    @ApiOperation(value = "添加评论")
    @PostMapping("/addComment")
    public R addComment(@RequestBody EduComment comment, HttpServletRequest request) {
        // 获取课程id和讲师id
        String teacherId = comment.getTeacherId();
        String courseId = comment.getCourseId();
        if (StringUtils.isEmpty(courseId) || StringUtils.isEmpty(teacherId)) {
            throw new GuLiException(ResultCode.ERROR, "课程参数异常，添加评论失败");
        }

        // 获取用户id
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        if (StringUtils.isEmpty(memberId)) {
            return R.error().code(28004).message("请先登录");
        }

        // 查询用户信息
        UcenterMemberWebVO memberInfo = ucenterClient.getMemberInfo(memberId);

        // 补充评论信息
        comment.setMemberId(memberId)
                .setNickname(memberInfo.getNickname())
                .setAvatar(memberInfo.getAvatar());

        boolean save = commentService.save(comment);
        if (save) {
            return R.ok()
                    .message("添加评论成功");
        } else {
            return R.error()
                    .message("添加评论失败");
        }
    }
}
