package com.lt.gulimall.product.dao;

import com.lt.gulimall.product.entity.AttrGroupEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lt.gulimall.product.vo.SkuItemGroupAttrVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 属性分组
 *
 * @author lt
 * @email 1746397845@qq.com
 * @date 2022-10-26 10:27:47
 */
@Mapper
public interface AttrGroupDao extends BaseMapper<AttrGroupEntity> {

    List<SkuItemGroupAttrVo> getAttrGroupWithAttrsBySpuId(Long spuId);
}
