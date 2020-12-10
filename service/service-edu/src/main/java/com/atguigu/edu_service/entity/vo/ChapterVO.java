package com.atguigu.edu_service.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author Administrator
 * @CreateTime 2020-11-25
 * @Description
 */
@Data
@ApiModel(value="Chapter视图对象", description="课程视图")
public class ChapterVO  implements Serializable {

    @ApiModelProperty(value = "章节ID")
    private String id;

    @ApiModelProperty(value = "章节名称")
    private String title;

    @ApiModelProperty(value = "显示排序")
    private Integer sort;

    @ApiModelProperty(value = "小节集合")
    private List<VideoVO> children = new ArrayList<>();
}
