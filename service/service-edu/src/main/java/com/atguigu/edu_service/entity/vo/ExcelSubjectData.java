package com.atguigu.edu_service.entity.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author Administrator
 * @CreateTime 2020-11-23
 * @Description
 */
@Data
public class ExcelSubjectData  implements Serializable {
    @ExcelProperty("一级分类")
    private String oneSubjectName;

    @ExcelProperty("二级分类")
    private String twoSubjectName;
}
