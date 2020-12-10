package com.atguigu.edu_service.controller;


import com.atguigu.common_utils.R;
import com.atguigu.edu_service.entity.EduChapter;
import com.atguigu.edu_service.entity.vo.ChapterVO;
import com.atguigu.edu_service.service.EduChapterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author marce
 * @since 2020-11-25
 */
@Api(description = "章节管理")
@RestController
@RequestMapping("/eduService/eduChapter")
//@CrossOrigin
public class EduChapterController {

    @Autowired
    private EduChapterService eduChapterService;

    @ApiOperation(value = "添加课时")
    @PostMapping("/addChapterInfo")
    public R addChapterInfo(@RequestBody EduChapter eduChapter) {
        boolean save = eduChapterService.save(eduChapter);
        if (save) {
            ChapterVO chapterVO = new ChapterVO();
            BeanUtils.copyProperties(eduChapter, chapterVO);
            return R.ok()
                    .data("chapterVOData", chapterVO);
        } else {
            return R.error()
                    .message("保存课时信息失败");
        }
    }

    @ApiOperation(value = "更新章节信息")
    @PutMapping("/updateChapterInfo")
    public R updateChapterInfo(@RequestBody EduChapter eduChapter) {
        boolean update = eduChapterService.updateById(eduChapter);
        if (update) {
            ChapterVO chapterVO = new ChapterVO();
            BeanUtils.copyProperties(eduChapter, chapterVO);
            return R.ok()
                    .data("data", chapterVO);
        } else {
            return R.error()
                    .message("更新课时信息失败");
        }
    }

    @ApiOperation(value = "根据章节id获取章节信息")
    @GetMapping("/getChapterInfo/{id}")
    public R getChapterInfo(@PathVariable("id") String id) {
        EduChapter eduChapter = eduChapterService.getById(id);
        if (eduChapter != null) {
            return R.ok()
                    .data("data", eduChapter);
        } else {
            return R.error()
                    .message("获取章节信息失败");
        }
    }

    @ApiOperation(value = "根据章节id删除章节")
    @DeleteMapping("deleteVideoInfo/{id}")
    public R deleteVideoInfo(@PathVariable("id") String id) {
        boolean remove = eduChapterService.removeById(id);
        if (remove) {
            return R.ok();
        } else {
            return R.error()
                    .message("删除章节失败");
        }
    }

    @ApiOperation(value = "根据课程id获取章节和课时信息")
    @GetMapping("/getChapterAndVideoInfo/{courseId}")
    public R getChapterAndVideoInfo(@PathVariable("courseId") String courseId) {
        List<ChapterVO> courseOutlineDataList = eduChapterService.getChapterAndVideoInfo(courseId);
        if (courseOutlineDataList != null) {
            return R.ok()
                    .data("data", courseOutlineDataList);
        } else {
            return R.error()
                    .message("获取课程大纲信息失败");
        }
    }

    @ApiOperation(value = "刷新章节和课时的排序")
    @PostMapping("/updateChapterAndVideoSort/{courseId}")
    public R updateChapterAndVideoSort(@PathVariable("courseId") String courseId,
                                       @RequestBody List<ChapterVO> chapterVOList) {

        eduChapterService.updateChapterAndVideoSort(courseId, chapterVOList);
        return R.ok();
    }
}

