package com.atguigu.edu_service.service;

import com.atguigu.edu_service.entity.EduSubject;
import com.atguigu.edu_service.entity.vo.SubjectVO;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 服务类
 * </p>
 *
 * @author marce
 * @since 2020-11-23
 */
public interface EduSubjectService extends IService<EduSubject> {

    void importSubjectData(MultipartFile file);

    List<SubjectVO> nestedList();
}
