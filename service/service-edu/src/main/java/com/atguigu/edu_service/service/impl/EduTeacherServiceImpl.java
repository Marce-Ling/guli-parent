package com.atguigu.edu_service.service.impl;

import com.atguigu.edu_service.entity.EduTeacher;
import com.atguigu.edu_service.entity.vo.TeacherQuery;
import com.atguigu.edu_service.mapper.EduTeacherMapper;
import com.atguigu.edu_service.service.EduTeacherService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 讲师 服务实现类
 * </p>
 *
 * @author marce
 * @since 2020-11-17
 */
@Service
public class EduTeacherServiceImpl extends ServiceImpl<EduTeacherMapper, EduTeacher> implements EduTeacherService {

//    @Resource
//    private EduTeacherMapper eduTeacherMapper;

    @Override
    public void getPage(Page<EduTeacher> pageParam, TeacherQuery teacherQuery) {
        QueryWrapper<EduTeacher> wrapper = new QueryWrapper<>();
        wrapper.orderByAsc("sort");

        if (teacherQuery == null) {
            baseMapper.selectPage(pageParam, wrapper);
            return ;
        }

        String name = teacherQuery.getName();
        Integer level = teacherQuery.getLevel();
        String beginTime = teacherQuery.getBeginTime();
        String endTime = teacherQuery.getEndTime();

        if (!StringUtils.isEmpty(name)) {
            wrapper.like("name", name);
        }

        if (!StringUtils.isEmpty(level)) {
            wrapper.eq("level", level);
        }

        if (!StringUtils.isEmpty(beginTime)) {
            wrapper.ge("gmt_create", beginTime);
        }

        if (!StringUtils.isEmpty(endTime)) {
            wrapper.le("gmt_create", endTime);
        }

        baseMapper.selectPage(pageParam, wrapper);
    }

    @Override
    public boolean removeById(Serializable id) {
        Integer result = baseMapper.deleteById(id);
        return null != result && result > 0;
    }
}
