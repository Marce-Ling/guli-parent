package com.atguigu.order_service.service;

import com.atguigu.order_service.entity.TOrder;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 订单 服务类
 * </p>
 *
 * @author marce
 * @since 2020-12-04
 */
public interface TOrderService extends IService<TOrder> {

    String saveOrder(String courseId, String memberId);
}
