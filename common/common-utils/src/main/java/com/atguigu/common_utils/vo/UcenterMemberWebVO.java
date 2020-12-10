package com.atguigu.common_utils.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author Administrator
 * @CreateTime 2020-12-4
 * @Description
 */
@Data
public class UcenterMemberWebVO {

    @ApiModelProperty(value = "会员id")
    private String id;

    @ApiModelProperty(value = "昵称")
    private String nickname;

    @ApiModelProperty(value = "用户头像")
    private String avatar;

    @ApiModelProperty(value = "手机号")
    private String mobile;

}
