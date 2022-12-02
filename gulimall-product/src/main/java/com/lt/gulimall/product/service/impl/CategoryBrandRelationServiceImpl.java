package com.lt.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.lt.gulimall.common.validator.group.UpdateGroup;
import com.lt.gulimall.product.service.BrandService;
import com.lt.gulimall.product.service.CategoryService;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lt.gulimall.common.utils.PageUtils;
import com.lt.gulimall.common.utils.Query;

import com.lt.gulimall.product.dao.CategoryBrandRelationDao;
import com.lt.gulimall.product.entity.CategoryBrandRelationEntity;
import com.lt.gulimall.product.service.CategoryBrandRelationService;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


@Service("categoryBrandRelationService")
public class CategoryBrandRelationServiceImpl extends ServiceImpl<CategoryBrandRelationDao, CategoryBrandRelationEntity> implements CategoryBrandRelationService {

    @Resource
    private CategoryService categoryService;

    @Resource
    private BrandService brandService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryBrandRelationEntity> page = this.page(
                new Query<CategoryBrandRelationEntity>().getPage(params),
                new QueryWrapper<CategoryBrandRelationEntity>()
        );

        return new PageUtils(page);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveDetail(CategoryBrandRelationEntity categoryBrandRelation) {
        categoryBrandRelation.setCatelogName(categoryService.getById(categoryBrandRelation.getCatelogId()).getName());
        categoryBrandRelation.setBrandName(brandService.getById(categoryBrandRelation.getBrandId()).getName());
        this.save(categoryBrandRelation);
    }

    @Override
    public void updateBrandNameByBrandId(Long brandId, String name) {
        CategoryBrandRelationEntity categoryBrandRelationEntity = new CategoryBrandRelationEntity();
        categoryBrandRelationEntity.setBrandName(name);
        categoryBrandRelationEntity.setBrandId(brandId);
        UpdateWrapper<CategoryBrandRelationEntity> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("brand_id",brandId);
        this.update(categoryBrandRelationEntity,updateWrapper);
    }

    @Override
    public void updateCatNameByCatId(Long catId, String name) {
        CategoryBrandRelationEntity categoryBrandRelationEntity = new CategoryBrandRelationEntity();
        categoryBrandRelationEntity.setCatelogName(name);
        categoryBrandRelationEntity.setCatelogId(catId);
        UpdateWrapper<CategoryBrandRelationEntity> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("catelog_id",catId);
        this.update(categoryBrandRelationEntity,updateWrapper);
    }
}
