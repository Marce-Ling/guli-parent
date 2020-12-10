package com.atguigu.edu_service.entity.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author Administrator
 * @CreateTime 2020-11-24
 * @Description
 */
@Data
public class SubjectVO  implements Serializable {

    private String id;

    private String title;

    private List<SubSubjectVO> children = new ArrayList<>();
}
