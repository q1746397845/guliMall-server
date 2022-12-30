package com.lt.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lt.gulimall.common.utils.PageUtils;
import com.lt.gulimall.product.entity.CategoryEntity;
import com.lt.gulimall.product.vo.Catalog2Vo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 商品三级分类
 *
 * @author lt
 * @email 1746397845@qq.com
 * @date 2022-10-26 10:27:47
 */
public interface CategoryService extends IService<CategoryEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<CategoryEntity> treeList();

    void removeMenuByIds(List<Long> catIdList);

    List<Long> queryFullCategoryPath(Long catelogId);

    void updateDetail(CategoryEntity category);

    List<CategoryEntity> getLevel1Category();

    Map<Long,List<Catalog2Vo>> getCatalogJson();
}

