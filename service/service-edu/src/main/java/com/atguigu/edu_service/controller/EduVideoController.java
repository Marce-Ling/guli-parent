package com.atguigu.edu_service.controller;


import com.atguigu.common_utils.R;
import com.atguigu.edu_service.entity.EduVideo;
import com.atguigu.edu_service.entity.vo.VideoVO;
import com.atguigu.edu_service.service.EduVideoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 课程视频 前端控制器
 * </p>
 *
 * @author marce
 * @since 2020-11-25
 */
@Api(description = "课时管理")
@RestController
@RequestMapping("/eduService/eduVideo")
//@CrossOrigin
public class EduVideoController {
    @Autowired
    private EduVideoService eduVideoService;

    @ApiOperation(value = "添加课时")
    @PostMapping("/addVideoInfo")
    public R addVideoInfo(@RequestBody EduVideo eduVideo) {
        boolean save = eduVideoService.save(eduVideo);
        if (save) {
            VideoVO videoVO = new VideoVO();
            BeanUtils.copyProperties(eduVideo, videoVO);
            return R.ok()
                    .data("videoVOData", videoVO);
        } else {
            return R.error().message("保存课时信息失败");
        }
    }

    @ApiOperation(value = "根据课时id获取课时信息")
    @GetMapping("/getVideoInfo/{id}")
    public R getVideoInfo(@PathVariable("id") String id) {
        EduVideo eduVideo = eduVideoService.getById(id);
        if (eduVideo != null) {
            VideoVO videoVO = new VideoVO();
            BeanUtils.copyProperties(eduVideo, videoVO);
            return R.ok()
                    .data("data", videoVO);
        } else {
            return R.error()
                    .message("获取课时信息失败");
        }
    }

    @ApiOperation(value = "更新课时信息")
    @PutMapping("/updateVideoInfo")
    public R updateVideoInfo(@RequestBody EduVideo eduVideo) {
        boolean update = eduVideoService.updateById(eduVideo);
        if (update) {
            VideoVO videoVO = new VideoVO();
            BeanUtils.copyProperties(eduVideo, videoVO);
            return R.ok()
                    .data("data", videoVO);
        } else {
            return R.error()
                    .message("更新课时信息失败");
        }
    }

    @ApiOperation(value = "根据章节id删除课时")
    @DeleteMapping("/deleteByChapterId/{chapterId}")
    public R deleteByChapterId(@PathVariable("chapterId") String chapterId) {
        boolean remove = eduVideoService.deleteByChapterId(chapterId);
        if (remove) {
            return R.ok();
        } else {
            return R.error()
                    .message("删除课时失败");
        }
    }

    @ApiOperation(value = "根据课时id删除课时")
    @DeleteMapping("/deleteById/{id}")
    public R deleteById(@PathVariable("id") String id) {
        boolean remove = eduVideoService.deleteById(id);
        if (remove) {
            return R.ok();
        } else {
            return R.error()
                    .message("删除课时失败");
        }
    }

}

