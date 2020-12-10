package com.atguigu.ucenter_service.service;

import com.atguigu.ucenter_service.entity.UcenterMember;
import com.atguigu.ucenter_service.entity.vo.LoginVO;
import com.atguigu.ucenter_service.entity.vo.RegisterVO;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 会员表 服务类
 * </p>
 *
 * @author marce
 * @since 2020-12-01
 */
public interface UcenterMemberService extends IService<UcenterMember> {

    void register(RegisterVO registerVO);

    String login(LoginVO loginVO);

}
