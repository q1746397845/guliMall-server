package com.lt.gulimall.product.dao;

import com.lt.gulimall.product.entity.AttrAttrgroupRelationEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lt.gulimall.product.vo.AttrGroupRelationVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.query.Param;

/**
 * 属性&属性分组关联
 *
 * @author lt
 * @email 1746397845@qq.com
 * @date 2022-10-26 10:27:47
 */
@Mapper
public interface AttrAttrgroupRelationDao extends BaseMapper<AttrAttrgroupRelationEntity> {

    void relationDelete(@Param("attrGroupRelationVos")AttrGroupRelationVo[] attrGroupRelationVos);
}
