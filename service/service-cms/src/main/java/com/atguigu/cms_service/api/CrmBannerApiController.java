package com.atguigu.cms_service.api;

import com.atguigu.cms_service.entity.CrmBanner;
import com.atguigu.cms_service.service.impl.CrmBannerServiceImpl;
import com.atguigu.common_utils.R;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author Administrator
 * @CreateTime 2020-11-30
 * @Description
 */
@Api(description = "网站首页Banner列表")
@RestController
@RequestMapping("/cmsService/banner")
//@CrossOrigin
public class CrmBannerApiController {

    @Autowired
    private CrmBannerServiceImpl crmBannerService;

    @ApiOperation(value = "获取所有横幅图片")
    @GetMapping("/getAllBanner")
    public R getAll(){
//        QueryWrapper<CrmBanner> bannerWrapper = new QueryWrapper<>();
//        bannerWrapper.orderByAsc("sort");
//        List<CrmBanner> bannerList = crmBannerService.list(bannerWrapper);
        List<CrmBanner> bannerList = crmBannerService.selectIndexList();
        return R.ok()
                .data("list", bannerList);
    }

}
