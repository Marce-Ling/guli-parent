package com.atguigu.ucenter_service.service.impl;

import com.atguigu.common_utils.JwtUtils;
import com.atguigu.common_utils.MD5Utils;
import com.atguigu.common_utils.ResultCode;
import com.atguigu.common_utils.constant.RedisConstant;
import com.atguigu.common_utils.exception.GuLiException;
import com.atguigu.ucenter_service.entity.UcenterMember;
import com.atguigu.ucenter_service.entity.vo.LoginVO;
import com.atguigu.ucenter_service.entity.vo.RegisterVO;
import com.atguigu.ucenter_service.mapper.UcenterMemberMapper;
import com.atguigu.ucenter_service.service.UcenterMemberService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author marce
 * @since 2020-12-01
 */
@Service
public class UcenterMemberServiceImpl extends ServiceImpl<UcenterMemberMapper, UcenterMember> implements UcenterMemberService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public void register(RegisterVO registerVO) {
        // 取参
        String phone = registerVO.getMobile();
        String nickname = registerVO.getNickname();
        String password = registerVO.getPassword();
        String code = registerVO.getCode();
        // 验空
        if (StringUtils.isEmpty(phone)
                || StringUtils.isEmpty(nickname)
                || StringUtils.isEmpty(password)
                || StringUtils.isEmpty(code)) {
            throw new GuLiException(ResultCode.ERROR, "注册信息不能为空");
        }
        // 验证验证码
        //      取出发送的验证码
        String dbCode = redisTemplate.opsForValue().get(RedisConstant.SMS_VERIFICATION_CODE + phone);
        //      验证
        if (!code.equals(dbCode)) {
            throw new GuLiException(ResultCode.ERROR, "验证码错误");
        }
        // 根据phone验证是否唯一
        QueryWrapper<UcenterMember> memberWrapper = new QueryWrapper<>();
        memberWrapper.ge("mobile", phone);
        Integer count = baseMapper.selectCount(memberWrapper);
        if (count != null && count > 0) {
            throw new GuLiException(ResultCode.ERROR, "该用户已注册");
        }
        // 明文加密
        String passwordMD5 = MD5Utils.md5(password);
        // 补全存储信息
        UcenterMember member = new UcenterMember();
        BeanUtils.copyProperties(registerVO, member);
        member.setPassword(passwordMD5);
        member.setAvatar("");
        member.setIsDisabled(false);
        // 存储
        baseMapper.insert(member);
    }

    @Override
    public String login(LoginVO loginVO) {
        // 取参
        String phone = loginVO.getMobile();
        String password = loginVO.getPassword();
        // 验空
        if (StringUtils.isEmpty(phone)
                || StringUtils.isEmpty(password)) {
            throw new GuLiException(ResultCode.ERROR, "登录信息不能为空");
        }
        // 根据phone验证是否有此用户
        QueryWrapper<UcenterMember> memberWrapper = new QueryWrapper<>();
        memberWrapper.ge("mobile", phone);
        UcenterMember member = baseMapper.selectOne(memberWrapper);
        if (member == null) {
            throw new GuLiException(ResultCode.ERROR, "该用户未注册");
        }
        // 验证密码
        String passwordMD5 = MD5Utils.md5(password);
        if (!passwordMD5.equals(member.getPassword())) {
            throw new GuLiException(ResultCode.ERROR, "用户名或密码错误");
        }
        // 校验是否被禁用
        if (member.getIsDisabled()) {
            throw new GuLiException(ResultCode.ERROR, "该用户已被封印");
        }

        // 允许登录，生成token
        String token = JwtUtils.getJwtToken(member.getId(), member.getNickname());

        return token;
    }


}
