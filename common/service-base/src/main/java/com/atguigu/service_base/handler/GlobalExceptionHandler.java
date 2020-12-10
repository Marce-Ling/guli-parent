package com.atguigu.service_base.handler;

import com.atguigu.common_utils.R;
import com.atguigu.common_utils.exception.GuLiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author Administrator
 * @CreateTime 2020-11-17
 * @Description 统一异常处理类
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(GuLiException.class)
    @ResponseBody
    public R error(GuLiException e) {
        e.printStackTrace();
//        log.error(e.getMessage());
        return R.error()
                .code(e.getCode())
                .message(e.getMessage());
    }
}
