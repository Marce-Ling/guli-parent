package com.atguigu.common_utils;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author Administrator
 * @CreateTime 2020-11-17
 * @Description
 */
@Data
@Accessors(chain = true)
public class R {

    @ApiModelProperty(value = "是否成功")
    private boolean success;

    @ApiModelProperty(value = "返回状态码")
    private Integer code;

    @ApiModelProperty(value = "返回消息")
    private String message;

    @ApiModelProperty(value = "返回数据")
    private Map<String, Object> data = new HashMap<>();

    public static R ok(){
        R r = new R();
        return r.setSuccess(true)
                .setCode(ResultCode.SUCCESS)
                .setMessage("成功");
    }

    public static R error(){
        R r = new R();
        return r.setSuccess(false)
                .setCode(ResultCode.ERROR)
                .setMessage("失败");
    }

    public R message(String message){
        return this.setMessage(message);
    }

    public R success(boolean success){
        return this.setSuccess(success);
    }

    public R code(Integer statusCode){
        return this.setCode(statusCode);
    }

    public R data(String key, Object value){
        this.data.put(key, value);
        return this;
    }

    public R data(Map<String, Object> dataMap){
        return this.setData(dataMap);
    }
}
