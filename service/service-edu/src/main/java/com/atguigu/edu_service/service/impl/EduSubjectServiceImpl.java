package com.atguigu.edu_service.service.impl;

import com.alibaba.excel.EasyExcel;
import com.atguigu.edu_service.entity.EduSubject;
import com.atguigu.edu_service.entity.vo.ExcelSubjectData;
import com.atguigu.edu_service.entity.vo.SubSubjectVO;
import com.atguigu.edu_service.entity.vo.SubjectVO;
import com.atguigu.edu_service.listener.SubjectExcelListener;
import com.atguigu.edu_service.mapper.EduSubjectMapper;
import com.atguigu.edu_service.service.EduSubjectService;
import com.atguigu.common_utils.exception.GuLiException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author marce
 * @since 2020-11-23
 */
@Service
public class EduSubjectServiceImpl extends ServiceImpl<EduSubjectMapper, EduSubject> implements EduSubjectService {

    @Override
    public void importSubjectData(MultipartFile file) {

        try {
            InputStream inputStream = file.getInputStream();
            EasyExcel
                    .read(inputStream, ExcelSubjectData.class, new SubjectExcelListener(this))
                    .sheet().doRead();
        } catch (IOException e) {
            e.printStackTrace();
            throw new GuLiException(20002,"添加课程分类失败");
        }
    }

    @Override
    public List<SubjectVO> nestedList() {

        // 创建封装返回信息的集合
        List<SubjectVO> list = new ArrayList<>();

        // 查询一级分类
        QueryWrapper<EduSubject> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id", "0").orderByAsc("sort");
        List<EduSubject> oneSubjectList = baseMapper.selectList(wrapper);

        // 查询二级分类
        wrapper = new QueryWrapper<>();
        wrapper.ne("parent_id", "0").orderByAsc("sort");
        List<EduSubject> twoSubjectList = baseMapper.selectList(wrapper);
        // 封装节点
        // 遍历一级分类，封装成节点，并找出各一级分类包含的二级分类
        for (EduSubject oneSubject : oneSubjectList) {
            // 父节点
            SubjectVO subjectVO = new SubjectVO();
            subjectVO.setId(oneSubject.getId());
            subjectVO.setTitle(oneSubject.getTitle());
            // 子节点集合
            List<SubSubjectVO> children = null;
            // 遍历二级分类，封装成节点，与一级分类绑定
            for (EduSubject twoSubject : twoSubjectList) {
                // 一级分类包含的二级分类
                if (oneSubject.getId().equals(twoSubject.getParentId())) {
                    if (children == null) {
                        children = new ArrayList<>();
                    }
                    // 子节点
                    SubSubjectVO subSubjectVO = new SubSubjectVO();
                    subSubjectVO.setId(twoSubject.getId());
                    subSubjectVO.setTitle(twoSubject.getTitle());
                    children.add(subSubjectVO);
                }
            }
            // 子节点与父节点绑定
            subjectVO.setChildren(children);
            // 打包
            list.add(subjectVO);
        }

        return list;
    }
}
