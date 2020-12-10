package com.atguigu.edu_service.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author Administrator
 * @CreateTime 2020-11-28
 * @Description
 */
@ApiModel(value = "course查询对象", description = "课程查询对象封装")
@Data
public class CourseQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(name = "subjectParentId", value = "一级分类id")
    private String subjectParentId;

    @ApiModelProperty(name = "subjectId", value = "二级分类id")
    private String subjectId;

    @ApiModelProperty(name = "title", value = "课程标题，模糊查询")
    private String title;

    @ApiModelProperty(name = "teacherId", value = "讲师id")
    private String teacherId;
}
