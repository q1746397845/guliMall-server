package com.lt.gulimall.product.service.impl;

import com.lt.gulimall.product.service.CategoryBrandRelationService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lt.gulimall.common.utils.PageUtils;
import com.lt.gulimall.common.utils.Query;

import com.lt.gulimall.product.dao.BrandDao;
import com.lt.gulimall.product.entity.BrandEntity;
import com.lt.gulimall.product.service.BrandService;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


@Service("brandService")
public class BrandServiceImpl extends ServiceImpl<BrandDao, BrandEntity> implements BrandService {

    @Resource
    private CategoryBrandRelationService categoryBrandRelationService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        QueryWrapper<BrandEntity> queryWrapper = new QueryWrapper<>();
        String key = (String) params.get("key");
        if(StringUtils.isNotBlank(key)){
            queryWrapper.eq("brand_id",key).or().like("name",key);
        }
        IPage<BrandEntity> page = this.page(
                new Query<BrandEntity>().getPage(params),queryWrapper
        );

        return new PageUtils(page);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateDetail(BrandEntity brand) {
        this.updateById(brand);
        if(StringUtils.isNotBlank(brand.getName())){
            //修改关联表数据
            categoryBrandRelationService.updateBrandNameByBrandId(brand.getBrandId(),brand.getName());
        }
    }

    @Override
    public List<BrandEntity> getInfoByIds(List<Long> brandIds) {
        QueryWrapper<BrandEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("brand_id",brandIds);
        List<BrandEntity> list = this.list(queryWrapper);
        return list;
    }
}
