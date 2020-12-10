package com.atguigu.common_utils.exception;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @Author Administrator
 * @CreateTime 2020-11-17
 * @Description 自定义异常类
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel(discriminator = "自定义异常类")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GuLiException extends RuntimeException{

    @ApiModelProperty(value = "状态码")
    private Integer code;

    @ApiModelProperty(value = "异常信息")
    private String message;

}
