package com.atguigu.wxlogin_service.api;

import com.atguigu.common_utils.JwtUtils;
import com.atguigu.common_utils.ResultCode;
import com.atguigu.common_utils.exception.GuLiException;
import com.atguigu.wxlogin_service.entity.UcenterMember;
import com.atguigu.wxlogin_service.service.UcenterMemberService;
import com.atguigu.wxlogin_service.utils.ConstantPropertiesUtil;
import com.atguigu.wxlogin_service.utils.HttpClientUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.Gson;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * @Author Administrator
 * @CreateTime 2020-12-1
 * @Description
 */
@Api(description = "微信扫码登录")
//@CrossOrigin
@Controller//注意这里没有配置 @RestController
@RequestMapping("/api/ucenter/wx")
public class WxApiController {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private UcenterMemberService memberService;


    @ApiOperation(value = "微信扫码登录")
    @GetMapping("login")
    public String genQrConnect(HttpSession session) {

        // 微信开放平台授权baseUrl
        String baseUrl = "https://open.weixin.qq.com/connect/qrconnect?" +
                "appid=%s" +
                "&redirect_uri=%s" +
                "&response_type=code" +
                "&scope=snsapi_login" +
                "&state=%s" +
                "#wechat_redirect";

        // 回调地址
        String redirectUrl = ConstantPropertiesUtil.WX_OPEN_REDIRECT_URL; //获取业务服务器重定向地址
        try {
            redirectUrl = URLEncoder.encode(redirectUrl, "UTF-8"); //url编码
        } catch (UnsupportedEncodingException e) {
            throw new GuLiException(20001, e.getMessage());
        }

        // 防止csrf攻击（跨站请求伪造攻击）
        //String state = UUID.randomUUID().toString().replaceAll("-", "");//一般情况下会使用一个随机数
        String state = "mywxatguigu";//为了让大家能够使用我搭建的外网的微信回调跳转服务器，这里填写你在ngrok的前置域名
//        System.out.println("state = " + state);

        // 采用redis等进行缓存state 使用sessionId为key 30分钟后过期，可配置
        //键："wechar-open-state-" + httpServletRequest.getSession().getId()
        //值：satte
        //过期时间：30分钟
        redisTemplate.opsForValue().setIfAbsent("wechar-open-state-" + session.getId(),
                state, 30L, TimeUnit.MINUTES);

        //生成qrcodeUrl
        String qrcodeUrl = String.format(
                baseUrl,
                ConstantPropertiesUtil.WX_OPEN_APP_ID,
                redirectUrl,
                state);

        return "redirect:" + qrcodeUrl;
    }

    @ApiOperation(value = "登录回调地址")
    @GetMapping("/callback")
    public String callback(String code, String state, HttpSession session){

        // 判断state
        String dbState = redisTemplate.opsForValue().get("wechar-open-state-" + session.getId());
        if (state == null || state.equals(dbState)) {
            throw new GuLiException(ResultCode.ERROR, "请求状态异常");
        }

        String baseAccessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token?" +
                "appid=%s" +
                "&secret=%s" +
                "&code=%s" +
                "&grant_type=authorization_code";

        String accessTokenUrl = String.format(
                baseAccessTokenUrl,
                ConstantPropertiesUtil.WX_OPEN_APP_ID,
                ConstantPropertiesUtil.WX_OPEN_APP_SECRET,
                code);

        // 发送get请求
        String result = null;

        try {
            result = HttpClientUtil.get(accessTokenUrl);
        } catch (Exception e) {
            throw new GuLiException(ResultCode.ERROR, "获取access_token失败");
        }

        // 解析请求返回值json字符串
        Gson gson = new Gson();
        HashMap map = gson.fromJson(result, HashMap.class);
        String accessToken = (String) map.get("access_token");
        String openid = (String) map.get("openid");

        // 查询数据库中是否已有此微信id
        QueryWrapper<UcenterMember> memberWrapper = new QueryWrapper<>();
        memberWrapper.eq("openid", openid);
        UcenterMember member = memberService.getOne(memberWrapper);

        // 没有此微信id，进行注册
        if (member == null) {

            //访问微信的资源服务器，获取用户信息
            String baseUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo" +
                    "?access_token=%s" +
                    "&openid=%s";

            String userInfoUrl = String.format(
                    baseUserInfoUrl,
                    accessToken,
                    openid);
            // 微信用户信息
            String resultUserInfo = null;
            try {
                // 访问微信的资源服务器，获取用户信息
                resultUserInfo = HttpClientUtil.get(userInfoUrl);
                System.out.println("resultUserInfo==========" + resultUserInfo);
            } catch (Exception e) {
                throw new GuLiException(20001, "获取用户信息失败");
            }

            // 解析json数据
            HashMap userInfoMap = gson.fromJson(resultUserInfo, HashMap.class);
            // 微信昵称
            String nickname = (String) userInfoMap.get("nickname");
            // 微信头像
            String headimgurl = (String)userInfoMap.get("headimgurl");

            // 补充数据
            member = new UcenterMember();
            member.setOpenid(openid);
            member.setAvatar(headimgurl);
            member.setNickname(nickname);
            member.setIsDisabled(false);
            // 存入数据库
            memberService.save(member);
        }

        // 生成登录token
        String token = JwtUtils.getJwtToken(member.getId(), member.getNickname());

        // 存入cookie
        // Cookie cookie = new Cookie;

        // 登录

        return "redirect:http://localhost:3000?token=" + token;
    }
}
