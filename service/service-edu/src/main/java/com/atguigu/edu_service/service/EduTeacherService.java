package com.atguigu.edu_service.service;

import com.atguigu.edu_service.entity.EduTeacher;
import com.atguigu.edu_service.entity.vo.TeacherQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 讲师 服务类
 * </p>
 *
 * @author marce
 * @since 2020-11-17
 */
public interface EduTeacherService extends IService<EduTeacher> {

    void getPage(Page<EduTeacher> pageParam, TeacherQuery teacherQuery);
}
