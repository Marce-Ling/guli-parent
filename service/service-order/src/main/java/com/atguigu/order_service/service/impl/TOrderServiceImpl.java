package com.atguigu.order_service.service.impl;

import com.atguigu.common_utils.exception.GuLiException;
import com.atguigu.common_utils.vo.CourseInfoWebVO;
import com.atguigu.common_utils.vo.UcenterMemberWebVO;
import com.atguigu.order_service.entity.TOrder;
import com.atguigu.order_service.feign.EduClient;
import com.atguigu.order_service.feign.UCenterClient;
import com.atguigu.order_service.mapper.TOrderMapper;
import com.atguigu.order_service.service.TOrderService;
import com.atguigu.order_service.utils.OrderNoUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 订单 服务实现类
 * </p>
 *
 * @author marce
 * @since 2020-12-04
 */
@Service
public class TOrderServiceImpl extends ServiceImpl<TOrderMapper, TOrder> implements TOrderService {

    @Autowired
    private EduClient eduClient;

    @Autowired
    private UCenterClient uCenterClient;

    @Override
    public String saveOrder(String courseId, String memberId) {
        // 查询课程信息
        CourseInfoWebVO courseInfoWeb = eduClient.getCourseInfoWeb(courseId);
        if (StringUtils.isEmpty(courseInfoWeb)) {
            throw new GuLiException(20001, "课程状态异常");
        }
        // 查询用户信息
        UcenterMemberWebVO memberInfo = uCenterClient.getMemberInfo(memberId);
        if (StringUtils.isEmpty(memberInfo)) {
            throw new GuLiException(20001, "会员状态异常");
        }
        // 生成订单号，补全订单信息
        String orderNo = OrderNoUtil.getOrderNo();
        TOrder order = new TOrder();
        order.setStatus(0)
                .setTotalFee(courseInfoWeb.getPrice())
                .setOrderNo(orderNo)
                .setMemberId(memberId)
                .setNickname(memberInfo.getNickname())
                .setMobile(memberInfo.getMobile())
                .setCourseId(courseId)
                .setCourseTitle(courseInfoWeb.getTitle())
                .setCourseCover(courseInfoWeb.getCover())
                .setTeacherName(courseInfoWeb.getTeacherName());

        // 存入数据库
        baseMapper.insert(order);
        return order.getOrderNo();
    }
}
