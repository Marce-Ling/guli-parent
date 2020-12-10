package com.atguigu.ucenter_service.controller;


import com.atguigu.ucenter_service.entity.UcenterMember;
import com.atguigu.ucenter_service.service.UcenterMemberService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author marce
 * @since 2020-12-01
 */
@Api(description = "用户中心")
@RestController
@RequestMapping("/ucenterService/backmember")
//@CrossOrigin
public class UcenterMemberController {

    @Autowired
    private UcenterMemberService memberService;


    @ApiOperation(value = "统计某日期下的注册会员数，远程调用")
    @GetMapping("/countRegisterByDate/{day}")
    public Integer countRegisterByDate(@PathVariable("day") String date) {
        QueryWrapper<UcenterMember> wrapper = new QueryWrapper<>();
        wrapper.likeRight("gmt_create", date);
        return memberService.count(wrapper);
    }

}

