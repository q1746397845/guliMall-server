package com.lt.gulimall.product.service.impl;

import com.lt.gulimall.product.service.CategoryBrandRelationService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lt.gulimall.common.utils.PageUtils;
import com.lt.gulimall.common.utils.Query;

import com.lt.gulimall.product.dao.CategoryDao;
import com.lt.gulimall.product.entity.CategoryEntity;
import com.lt.gulimall.product.service.CategoryService;

import javax.annotation.Resource;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Resource
    private CategoryBrandRelationService categoryBrandRelationService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<CategoryEntity> treeList() {
        List<CategoryEntity> categoryEntities = baseMapper.selectList(null);
        List<CategoryEntity> level1Categorys = categoryEntities.stream()
                .filter(category -> category.getParentCid().equals(0L))
                .sorted(Comparator.comparingInt(CategoryEntity::getSort)).collect(Collectors.toList());
        level1Categorys.stream().forEach(category -> category.setChildren(getChildrens(categoryEntities,category)));
        return level1Categorys;
    }

    @Override
    public void removeMenuByIds(List<Long> catIdList) {
        baseMapper.deleteBatchIds(catIdList);
    }

    @Override
    public List<Long> queryFullCategoryPath(Long catelogId) {
        List<Long> list = new ArrayList<>();
        queryParentPath(catelogId,list);
        Collections.reverse(list);
        return list;
    }

    @Override
    public void updateDetail(CategoryEntity category) {
        this.updateById(category);
        if(StringUtils.isNotBlank(category.getName())){
            //修改关联表
            categoryBrandRelationService.updateCatNameByCatId(category.getCatId(),category.getName());
        }
    }

    private void queryParentPath(Long catelogId, List<Long> list){
        list.add(catelogId);
        CategoryEntity categoryEntity = baseMapper.selectById(catelogId);
        if(categoryEntity.getParentCid() != 0){
            queryParentPath(categoryEntity.getParentCid(),list);
        }
    }

    private List<CategoryEntity> getChildrens(List<CategoryEntity> allCategoryList, CategoryEntity parentCategory){
        List<CategoryEntity> collect = allCategoryList.stream()
                .filter(category -> category.getParentCid().equals(parentCategory.getCatId()))
                .map(category -> {
                    category.setChildren(getChildrens(allCategoryList, category));
                    return category;
                }).sorted(Comparator.comparingInt(CategoryEntity::getSort)).collect(Collectors.toList());
        return collect;
    }
}
