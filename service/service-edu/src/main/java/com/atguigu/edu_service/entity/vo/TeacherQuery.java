package com.atguigu.edu_service.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author Administrator
 * @CreateTime 2020-11-17
 * @Description
 */
@ApiModel(value = "Teacher查询对象", description = "讲师查询对象封装")
@Data
public class TeacherQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(name = "name", value = "讲师名称，模糊查询")
    private String name;

    @ApiModelProperty(name = "level", value = "头衔 1:高级讲师 2:首席讲师")
    private Integer level;

    @ApiModelProperty(name = "beginTime", value = "查询开始时间", example = "2019-01-01 10:10:10")
    private String beginTime;

    @ApiModelProperty(name = "endTime", value = "查询结束时间", example = "2019-12-01 10:10:10")
    private String endTime;
}
