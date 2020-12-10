package com.atguigu.order_service.controller;


import com.atguigu.common_utils.R;
import com.atguigu.order_service.service.TPayLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 支付日志表 前端控制器
 * </p>
 *
 * @author marce
 * @since 2020-12-04
 */
@Api(description = "订单支付日志管理")
@RestController
@RequestMapping("/orderservice/paylog")
//@CrossOrigin
public class TPayLogController {

    @Autowired
    private TPayLogService payLogService;

    @ApiOperation(value = "生成微信支付二维码")
    @GetMapping("/generatePayQrCode/{orderNo}")
    public R generatePayQrCode(@PathVariable("orderNo") String orderNo) {
        Map<String, Object> result = payLogService.generatePayQrCode(orderNo);
        return R.ok()
                .data(result);
    }

    @ApiOperation(value = "根据订单号查询订单支付状态")
    @GetMapping("/queryPayStatus/{orderNo}")
    public R queryPayStatus(@PathVariable("orderNo") String orderNo) {
        Map<String, String> result = payLogService.queryPayStatus(orderNo);
        if (result == null) {
            return R.error().message("支付出错");
        }
        if ("SUCCESS".equals(result.get("trade_state"))) {
            //更改订单状态
            payLogService.updateOrderStatus(result);
            return R.ok().message("支付成功");
        }

        return R.error().code(25000).message("支付中");
    }

}

