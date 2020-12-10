package com.atguigu.ucenter_service.api;

import com.atguigu.common_utils.JwtUtils;
import com.atguigu.common_utils.R;
import com.atguigu.common_utils.vo.UcenterMemberWebVO;
import com.atguigu.ucenter_service.entity.UcenterMember;
import com.atguigu.ucenter_service.entity.vo.LoginInfoVO;
import com.atguigu.ucenter_service.entity.vo.LoginVO;
import com.atguigu.ucenter_service.entity.vo.RegisterVO;
import com.atguigu.ucenter_service.service.UcenterMemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author Administrator
 * @CreateTime 2020-12-1
 * @Description
 */
@Api(description = "用户/客户管理")
@RestController
@RequestMapping("/ucenterService/apiMember")
//@CrossOrigin
public class UcenterMemberApiController {

    @Autowired
    private UcenterMemberService memberService;

    @ApiOperation(value = "注册")
    @PostMapping("/register")
    public R register(@RequestBody RegisterVO registerVO) {
        memberService.register(registerVO);
        return R.ok();
    }

    @ApiOperation(value = "登录")
    @PostMapping("/login")
    public R login(@RequestBody LoginVO loginVO) {
        String token = memberService.login(loginVO);
        return R.ok()
                .data("token", token);
    }

    @ApiOperation(value = "根据token获取登录信息")
    @GetMapping("/getLoginInfo")
    public R getLoginInfo(HttpServletRequest request) {
        // 获取token参数
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        UcenterMember member = memberService.getById(memberId);
        LoginInfoVO loginInfoVO = new LoginInfoVO();
        BeanUtils.copyProperties(member, loginInfoVO);
        return R.ok()
                .data("loginInfo", loginInfoVO);
    }

    @ApiOperation(value = "根据id获取用户信息，远程调用")
    @GetMapping("/getMemberInfo/{id}")
    public UcenterMemberWebVO getMemberInfo(@PathVariable("id") String id) {
        UcenterMember member = memberService.getById(id);
        UcenterMemberWebVO memberWebVO = new UcenterMemberWebVO();
        BeanUtils.copyProperties(member, memberWebVO);
        return memberWebVO;
    }

}
