package com.atguigu.edu_service.entity.web_vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * @Author Administrator
 * @CreateTime 2020-12-3
 * @Description
 */
@Data
public class CourseQueryWeb implements Serializable {

    @ApiModelProperty(value = "课程ID")
    private String id;

    @ApiModelProperty(value = "课程专业ID")
    private String subjectId;

    @ApiModelProperty(value = "课程专业父级ID")
    private String subjectParentId;

    @ApiModelProperty(value = "销售数量是否降序，null不以此排序，false升序，true降序")
    private Boolean buyCountByDesc;

    @ApiModelProperty(value = "更新时间是否降序，null不以此排序，false升序，true降序")
    private Boolean gmtModifiedByDesc;

    @ApiModelProperty(value = "课程销售价格是否降序，null不以此排序，false升序，true降序")
    private Boolean priceByDesc;
}
