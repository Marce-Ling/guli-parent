package com.atguigu.edu_service.controller;

import com.atguigu.common_utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

/**
 * @Author Administrator
 * @CreateTime 2020-11-21
 * @Description
 */
@Api(description = "模拟登录")
@RestController
@RequestMapping("/eduService/user")
//@CrossOrigin
public class UserController {

    @ApiOperation(value = "登录")
    @PostMapping("/login")
    public R login() {
        return R.ok().data("token","admin");
    }

    //{"code":20000,"data":{"roles":["admin"],"name":"admin",
    // (头像)"avatar":"https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif"}}
    @ApiOperation(value = "用户信息")
    @GetMapping("/info")
    public R info(){
        return R.ok()
                .data("roles", "admin")
                .data("name", "admin")
                .data("avatar", "https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
    }
}
