package com.atguigu.edu_service.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author Administrator
 * @CreateTime 2020-11-24
 * @Description
 */
@Data
@ApiModel(value="EduCourseIntro对象", description="课程简介")
public class EduCourseIntro  implements Serializable {

    @ApiModelProperty(value = "课程ID")
    private String id;

    @ApiModelProperty(value = "课程简介")
    private String description;

    @ApiModelProperty(value = "创建时间")
    private Date gmtCreate;

    @ApiModelProperty(value = "更新时间")
    private Date gmtModified;

}
