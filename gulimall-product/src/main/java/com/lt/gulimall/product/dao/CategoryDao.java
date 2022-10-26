package com.lt.gulimall.product.dao;

import com.lt.gulimall.product.entity.CategoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品三级分类
 * 
 * @author lt
 * @email 1746397845@qq.com
 * @date 2022-10-26 10:27:47
 */
@Mapper
public interface CategoryDao extends BaseMapper<CategoryEntity> {
	
}
