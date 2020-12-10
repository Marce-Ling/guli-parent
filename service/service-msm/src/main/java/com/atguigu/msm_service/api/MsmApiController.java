package com.atguigu.msm_service.api;

import com.atguigu.common_utils.R;
import com.atguigu.msm_service.utils.RandomUtil;
import com.atguigu.common_utils.constant.RedisConstant;
import com.atguigu.msm_service.service.MsmService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Author Administrator
 * @CreateTime 2020-12-1
 * @Description
 */
@Api(description = "短信管理")
@RestController
@RequestMapping("/msmService/msm")
//@CrossOrigin
public class MsmApiController {

    @Autowired
    private MsmService msmService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @ApiOperation(value = "发送验证码")
    @GetMapping("/send/{phone}")
    public R send(@PathVariable("phone") String phone){
        // 验空
        if (StringUtils.isEmpty(phone)) return R.error().message("手机号不能为空");
        // 是否已发送
        String code = redisTemplate.opsForValue().get(RedisConstant.SMS_VERIFICATION_CODE + phone);
        if (!StringUtils.isEmpty(code)) return R.ok().message("验证码已发送");

        // 生成随机验证码
        code = RandomUtil.generateValidateCode(4).toString();
        Map<String,Object> param = new HashMap<>();
        param.put("code", code);
        boolean isSend = msmService.send(phone, "SMS_205136919", param);
        if(isSend) {
            redisTemplate.opsForValue().set(
                    RedisConstant.SMS_VERIFICATION_CODE + phone,
                    code,5, TimeUnit.MINUTES);
            return R.ok();
        } else {
            return R.error().message("发送短信失败");
        }
    }
}
