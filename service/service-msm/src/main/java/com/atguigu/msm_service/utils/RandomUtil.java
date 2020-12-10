package com.atguigu.msm_service.utils;

import java.util.Random;

/**
 * @Author Administrator
 * @CreateTime 2020-12-1
 * @Description 随机生成验证码工具类
 */
public class RandomUtil {

    /**
     * 随机生成验证码
     * @param length 长度为4位或者6位
     * @return
     */
    public static Integer generateValidateCode(int length){
        Integer code = null;
        if (4 == length) {
            code = new Random().nextInt(8999) + 1000;
        } else if (6 == length) {
            code = new Random().nextInt(899999) + 100000;
        } else {
            throw new RuntimeException("只能生成4位或6位数字验证码");
        }
        return code;
    }

    /**
     * 随机生成指定长度字符串验证码
     * @param length 长度
     * @return
     */
    public static String generateValidateCode4String(int length){
        Random rdm = new Random();
        String hash1 = Integer.toHexString(rdm.nextInt());
        String codeStr = hash1.substring(0, length);
        return codeStr;
    }
}
