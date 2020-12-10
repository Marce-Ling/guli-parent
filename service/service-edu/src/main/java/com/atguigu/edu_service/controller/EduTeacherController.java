package com.atguigu.edu_service.controller;


import com.atguigu.common_utils.R;
import com.atguigu.common_utils.constant.RedisConstant;
import com.atguigu.edu_service.entity.EduTeacher;
import com.atguigu.edu_service.entity.vo.TeacherQuery;
import com.atguigu.edu_service.service.EduTeacherService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author marce
 * @since 2020-11-17
 */
@Api(description = "讲师管理")
@RestController
@RequestMapping("/eduService/eduTeacher")
//@CrossOrigin
public class EduTeacherController {

    @Autowired
    private EduTeacherService eduTeacherService;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @ApiOperation(value = "获取所有讲师列表")
    @GetMapping("/getAll")
    public R getAll(){
        List<EduTeacher> list = eduTeacherService.list(null);
        return R.ok().data("list", list);
    }

    @ApiOperation(value = "根据id删除讲师信息")
    @DeleteMapping("/delById/{id}")
    public R delById(
            @ApiParam(name = "id", value = "讲师id", required = true)
            @PathVariable("id") String id){
        boolean remove = eduTeacherService.removeById(id);
        if (remove) {
            return R.ok();
        } else {
            return R.error();
        }
    }

    @ApiOperation(value = "分页讲师列表")
    @GetMapping("/getPage/{current}/{limit}")
    public R getPage(
            @ApiParam(name = "current", value = "当前页", required = true)
            @PathVariable("current") Long current,
            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable("limit") Long limit,
            @ApiParam(name = "teacherQuery", value = "查询条件")
            TeacherQuery teacherQuery){

        Page<EduTeacher> pageParam = new Page<>(current, limit);

        eduTeacherService.getPage(pageParam, teacherQuery);

        return R.ok()
                .data("list", pageParam.getRecords())
                .data("total", pageParam.getTotal());
    }

    @ApiOperation(value = "新增讲师")
    @PostMapping("/add")
    public R add(
            @ApiParam(name = "eduTeacher", value = "讲师对象", required = true)
            @RequestBody EduTeacher eduTeacher){

        boolean save = eduTeacherService.save(eduTeacher);
        if (save) {
            String avatarUrl = eduTeacher.getAvatar();
            redisTemplate.boundSetOps(RedisConstant.EDU_TEACHER_PIC_DB_RESOURCES).add(avatarUrl);
            return R.ok();
        } else {
            return R.error();
        }
    }

    @ApiOperation(value = "根据id获取讲师信息")
    @GetMapping("/getById/{id}")
    public R getById(
            @ApiParam(name = "id", value = "讲师id", required = true)
            @PathVariable("id") String id){
        EduTeacher teacher = eduTeacherService.getById(id);
        return R.ok()
                .data("teacher", teacher);
    }

    @ApiOperation(value = "根据id更新讲师信息")
    @PutMapping("/update")
    public R update(
            @ApiParam(name = "eduTeacher", value = "讲师对象", required = true)
            @RequestBody EduTeacher eduTeacher){
        boolean update = eduTeacherService.updateById(eduTeacher);
        if (update) {
            String avatarUrl = eduTeacher.getAvatar();
            redisTemplate.boundSetOps(RedisConstant.EDU_TEACHER_PIC_DB_RESOURCES).add(avatarUrl);
            return R.ok();
        } else {
            return R.error();
        }
    }


}

