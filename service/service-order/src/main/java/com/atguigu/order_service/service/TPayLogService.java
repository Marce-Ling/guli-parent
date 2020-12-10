package com.atguigu.order_service.service;

import com.atguigu.order_service.entity.TPayLog;
import com.baomidou.mybatisplus.extension.service.IService;

import java.text.ParseException;
import java.util.Map;

/**
 * <p>
 * 支付日志表 服务类
 * </p>
 *
 * @author marce
 * @since 2020-12-04
 */
public interface TPayLogService extends IService<TPayLog> {

    Map<String, Object> generatePayQrCode(String orderNo);

    Map<String, String> queryPayStatus(String orderNo);

    void updateOrderStatus(Map<String, String> result);
}
