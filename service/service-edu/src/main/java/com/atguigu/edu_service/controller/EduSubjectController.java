package com.atguigu.edu_service.controller;


import com.atguigu.common_utils.R;
import com.atguigu.edu_service.entity.vo.SubjectVO;
import com.atguigu.edu_service.service.EduSubjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程科目 前端控制器
 * </p>
 *
 * @author marce
 * @since 2020-11-23
 */
@Api(description = "课程分类管理")
@RestController
@RequestMapping("/eduService/eduSubject")
//@CrossOrigin
public class EduSubjectController {

    @Autowired
    private EduSubjectService eduSubjectService;

    @ApiOperation(value = "Excel批量导入课程分类")
    @PostMapping("/addSubject")
    public R addSubject(@ApiParam(value = "Excel文件") MultipartFile file){
        eduSubjectService.importSubjectData(file);
        return R.ok().message("导入成功");
    }

    @ApiOperation(value = "获取课程分类")
    @GetMapping("/getNestedList")
    public R getNestedList(){

        List<SubjectVO> result = eduSubjectService.nestedList();

        return R.ok()
                .data("data", result);
    }

}

