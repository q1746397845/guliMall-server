package com.lt.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lt.gulimall.common.utils.PageUtils;
import com.lt.gulimall.product.entity.AttrEntity;
import com.lt.gulimall.product.entity.ProductAttrValueEntity;
import com.lt.gulimall.product.vo.AttrResponseVo;
import com.lt.gulimall.product.vo.AttrVo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 商品属性
 *
 * @author lt
 * @email 1746397845@qq.com
 * @date 2022-10-26 10:27:47
 */
public interface AttrService extends IService<AttrEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveAttr(AttrVo attrVo);

    PageUtils baseList(Map<String, Object> params,Long catelogId,String type);

    AttrResponseVo getDetail(Long attrId);

    void updateAttr(AttrVo attrVo);

    List<AttrEntity> getRelationAttr(Long attrGroupId);

    PageUtils getNoattrRelation(Long attrGroupId,Map<String, Object> params);

    List<Long> selectSearchAttrs(List<Long> attrIds);
}

