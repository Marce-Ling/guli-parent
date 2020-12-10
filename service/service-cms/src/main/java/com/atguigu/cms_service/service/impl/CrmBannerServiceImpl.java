package com.atguigu.cms_service.service.impl;

import com.atguigu.cms_service.entity.CrmBanner;
import com.atguigu.cms_service.mapper.CrmBannerMapper;
import com.atguigu.cms_service.service.CrmBannerService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 首页banner表 服务实现类
 * </p>
 *
 * @author marce
 * @since 2020-11-30
 */
@Service
public class CrmBannerServiceImpl extends ServiceImpl<CrmBannerMapper, CrmBanner> implements CrmBannerService {

    @Cacheable(value = "banner", key = "'selectIndexList'")
    @Override
    public List<CrmBanner> selectIndexList() {
        List<CrmBanner> sort = baseMapper.selectList(new QueryWrapper<CrmBanner>()
                .orderByAsc("sort"));
        return sort;
    }
}
