package com.atguigu.order_service.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.order_service.entity.TOrder;
import com.atguigu.order_service.entity.TPayLog;
import com.atguigu.order_service.mapper.TPayLogMapper;
import com.atguigu.order_service.service.TOrderService;
import com.atguigu.order_service.service.TPayLogService;
import com.atguigu.order_service.utils.HttpClient;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.wxpay.sdk.WXPayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 支付日志表 服务实现类
 * </p>
 *
 * @author marce
 * @since 2020-12-04
 */
@Service
public class TPayLogServiceImpl extends ServiceImpl<TPayLogMapper, TPayLog> implements TPayLogService {

    @Autowired
    private TOrderService orderService;

    @Override
    public Map<String, Object> generatePayQrCode(String orderNo) {
        // 封装返回结果的集合
        Map<String, Object> map = new HashMap<>();

        try {
            //查询订单信息
            QueryWrapper<TOrder> orderWrapper = new QueryWrapper<>();
            orderWrapper.eq("order_no", orderNo);
            TOrder order = orderService.getOne(orderWrapper);

            Map<String, String> paramMap = new HashMap();
            //1、设置支付参数
            paramMap.put("appid", "wx74862e0dfcf69954");
            paramMap.put("mch_id", "1558950191");
            paramMap.put("nonce_str", WXPayUtil.generateNonceStr());
            paramMap.put("body", "购买《" + order.getCourseTitle() + "》guli课程");
            paramMap.put("out_trade_no", order.getOrderNo());
            paramMap.put("total_fee",
                    order.getTotalFee().multiply(new BigDecimal("100")).longValue() + "");
            paramMap.put("spbill_create_ip", "127.0.0.1");
            paramMap.put("notify_url", "http://guli.shop/api/order/weixinPay/weixinNotify");
            paramMap.put("trade_type", "NATIVE");


            //2、使用HttpClient根据URL访问第三方接口并且传递参数
            HttpClient client = new HttpClient(
                    "https://api.mch.weixin.qq.com/pay/unifiedorder");
            //  2.1 设置参数
            client.setXmlParam(WXPayUtil.generateSignedXml(paramMap,
                    "T6m9iK73b0kn9g5v426MKfHQH7X8rKwb"));
            client.setHttps(true);
            //  2.2 发送请求
            client.post();
            //3、 获取第三方响应数据
            String content = client.getContent();
            //  解析响应数据
            Map<String, String> resultMap = WXPayUtil.xmlToMap(content);

            //4、封装返回结果
            map.put("out_trade_no", order.getOrderNo());
            map.put("total_fee", order.getTotalFee());
            map.put("course_id", order.getCourseId());
            map.put("return_code", resultMap.get("return_code"));
            map.put("code_url", resultMap.get("code_url"));

            //微信支付二维码2小时过期，可采取2小时未支付取消订单
            //redisTemplate.opsForValue().set(orderNo, map, 120, TimeUnit.MINUTES);

            return map;
        } catch (Exception e) {
            e.printStackTrace();
            return map;
        }
    }

    @Override
    public Map<String, String> queryPayStatus(String orderNo) {

        try {
            Map<String, String> paramMap = new HashMap();
            //1、设置支付参数
            paramMap.put("appid", "wx74862e0dfcf69954");
            paramMap.put("mch_id", "1558950191");
            paramMap.put("nonce_str", WXPayUtil.generateNonceStr());
            paramMap.put("out_trade_no", orderNo);

            //2、使用HttpClient根据URL访问第三方接口并且传递参数
            HttpClient client = new HttpClient(
                    "https://api.mch.weixin.qq.com/pay/orderquery");
            //  2.1 设置参数
            client.setXmlParam(WXPayUtil.generateSignedXml(paramMap,
                    "T6m9iK73b0kn9g5v426MKfHQH7X8rKwb"));
            client.setHttps(true);
            //  2.2 发送请求
            client.post();
            //3、 获取第三方响应数据
            String content = client.getContent();
            //  解析响应数据
            Map<String, String> resultMap = WXPayUtil.xmlToMap(content);

            return resultMap;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;

    }


    //更改订单状态
    @Override
    public void updateOrderStatus(Map<String, String> result) {
        //查询订单信息
        QueryWrapper<TOrder> orderWrapper = new QueryWrapper<>();
        orderWrapper.eq("order_no", result.get("out_trade_no"));
        TOrder order = orderService.getOne(orderWrapper);

        if (1 == order.getStatus()) return;

        // 更新订单状态
        order.setStatus(1).setPayType(1);
        orderService.updateById(order);

        // 记录日志
        TPayLog payLog = new TPayLog();
        payLog.setOrderNo(order.getOrderNo()) // 订单号
                .setPayTime(order.getGmtModified()) // 支付完成时间
                .setTotalFee(order.getTotalFee()) // 支付金额（分）
                .setTransactionId(result.get("transaction_id")) // 交易流水号
                .setTradeState(result.get("trade_state")) // 交易状态
                .setPayType(1) // 支付类型（1：微信 2：支付宝）
                .setAttr(JSONObject.toJSONString(result)); // 其他属性
        baseMapper.insert(payLog);
    }
}
