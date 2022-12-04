package com.lt.gulimall.ware.dao;

import com.lt.gulimall.ware.entity.WareSkuEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 商品库存
 *
 * @author lt
 * @email 1746397845@qq.com
 * @date 2022-10-26 12:20:23
 */
@Mapper
public interface WareSkuDao extends BaseMapper<WareSkuEntity> {

    List<WareSkuEntity> selectSkuStockBySkuIds(@Param("skuIds")List<Long> skuIds);
}
