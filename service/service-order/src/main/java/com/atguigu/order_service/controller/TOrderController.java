package com.atguigu.order_service.controller;


import com.atguigu.common_utils.JwtUtils;
import com.atguigu.common_utils.R;
import com.atguigu.order_service.entity.TOrder;
import com.atguigu.order_service.service.TOrderService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 订单 前端控制器
 * </p>
 *
 * @author marce
 * @since 2020-12-04
 */
@Api(description = "订单管理")
@RestController
@RequestMapping("/orderservice/ordermanage")
//@CrossOrigin
public class TOrderController {

    @Autowired
    private TOrderService orderService;

    @ApiOperation(value = "根据课程id和用户id生成订单")
    @PostMapping("/createOrder/{courseId}")
    public R createOrder(@PathVariable("courseId") String courseId, HttpServletRequest request) {
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        if (StringUtils.isEmpty(memberId)) return R.error().code(28004).message("请先登录");
        String orderNo = orderService.saveOrder(courseId, memberId);
        return R.ok()
                .data("orderNo", orderNo);
    }

    @ApiOperation(value = "根据订单号获取订单信息")
    @GetMapping("/getOrderInfoByNo/{orderNo}")
    public R getOrderInfoByNo(@PathVariable("orderNo") String orderNo) {
        QueryWrapper<TOrder> wrapper = new QueryWrapper<>();
        wrapper.eq("order_no", orderNo);
        TOrder order = orderService.getOne(wrapper);
        return R.ok()
                .data("orderInfo", order);
    }

    @ApiOperation(value = "根据课程id和用户id获取已完成订单信息")
    @GetMapping("/getOrderInfoByNo/{courseId}/{memberId}")
    public boolean getBuyCourse(
            @PathVariable("courseId") String courseId,
            @PathVariable("memberId") String memberId) {

        QueryWrapper<TOrder> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id", courseId)
                .eq("member_id", memberId)
                .eq("status", 1);

        int count = orderService.count(wrapper);

        return count > 0;
    }
}
