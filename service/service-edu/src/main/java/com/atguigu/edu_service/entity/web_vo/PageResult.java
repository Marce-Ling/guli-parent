package com.atguigu.edu_service.entity.web_vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.apache.poi.ss.formula.functions.T;

import java.io.Serializable;
import java.util.List;

/**
 * @Author Administrator
 * @CreateTime 2020-12-2
 * @Description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class PageResult implements Serializable {

    @ApiModelProperty(value = "当前页")
    private Long current;

    @ApiModelProperty(value = "每页记录数")
    private Long size;

    @ApiModelProperty(value = "总页数")
    private Long pages;

    @ApiModelProperty(value = "总记录数")
    private Long total;

    @ApiModelProperty(value = "分页列表信息")
    private List dataList;

    @ApiModelProperty(value = "是否有下一页")
    private boolean hasNext;

    @ApiModelProperty(value = "是否有上一页")
    private boolean hasPrevious;
}
