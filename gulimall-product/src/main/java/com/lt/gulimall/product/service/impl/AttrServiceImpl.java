package com.lt.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.lt.gulimall.common.constant.ProductConstant;
import com.lt.gulimall.product.dao.AttrAttrgroupRelationDao;
import com.lt.gulimall.product.entity.*;
import com.lt.gulimall.product.service.*;
import com.lt.gulimall.product.vo.AttrResponseVo;
import com.lt.gulimall.product.vo.AttrVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lt.gulimall.common.utils.PageUtils;
import com.lt.gulimall.common.utils.Query;

import com.lt.gulimall.product.dao.AttrDao;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;


@Service("attrService")
public class AttrServiceImpl extends ServiceImpl<AttrDao, AttrEntity> implements AttrService {

    @Resource
    private AttrAttrgroupRelationService attrAttrgroupRelationService;

    @Resource
    private CategoryService categoryService;

    @Resource
    private AttrGroupService attrGroupService;

    @Resource
    private AttrDao attrDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                new QueryWrapper<AttrEntity>()
        );

        return new PageUtils(page);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveAttr(AttrVo attrVo) {
        AttrEntity attrEntity = new AttrEntity();
        BeanUtils.copyProperties(attrVo,attrEntity);
        this.save(attrEntity);
        if(attrEntity.getAttrType().equals(ProductConstant.AttrTypeEnum.ATTR_TYPE_BASE.getCode()) && attrVo.getAttrGroupId() != null) {
            AttrAttrgroupRelationEntity attrAttrgroupRelationEntity = new AttrAttrgroupRelationEntity();
            attrAttrgroupRelationEntity.setAttrGroupId(attrVo.getAttrGroupId());
            attrAttrgroupRelationEntity.setAttrId(attrEntity.getAttrId());
            attrAttrgroupRelationService.save(attrAttrgroupRelationEntity);
        }
    }


    @Override
    public PageUtils baseList(Map<String, Object> params,Long catelogId,String type) {
        QueryWrapper<AttrEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("attr_type", ProductConstant.AttrTypeEnum.getCode(type));
        if(catelogId != 0){
            queryWrapper.eq("catelog_id",catelogId);
        }
        String key = (String) params.get("key");
        if(StringUtils.isNotBlank(key)){
            queryWrapper.and(obj ->{
                obj.eq("attr_id",key).or().like("attr_name",key);
            });
        }

        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),queryWrapper
        );
        List<AttrEntity> records = page.getRecords();
        List<AttrResponseVo> collect = records.stream().map(record -> {
            AttrResponseVo attrResponseVo = new AttrResponseVo();
            BeanUtils.copyProperties(record, attrResponseVo);
            if(type.equals(ProductConstant.AttrTypeEnum.ATTR_TYPE_BASE.getType())){
                AttrAttrgroupRelationEntity entity = attrAttrgroupRelationService.getOne(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id",record.getAttrId()));
                if(entity != null){
                    attrResponseVo.setAttrGroupId(entity.getAttrGroupId());
                    AttrGroupEntity attrGroup = attrGroupService.getById(entity.getAttrGroupId());
                    if(attrGroup != null){
                        attrResponseVo.setGroupName(attrGroup.getAttrGroupName());
                    }
                }
            }

            CategoryEntity category = categoryService.getById(record.getCatelogId());
            if(category != null){
                attrResponseVo.setCatelogName(category.getName());
            }
            return attrResponseVo;
        }).collect(Collectors.toList());
        PageUtils pageUtils = new PageUtils(page);
        pageUtils.setList(collect);
        return pageUtils;
    }

    @Override
    public AttrResponseVo getDetail(Long attrId) {
        AttrEntity attrEntity = this.getById(attrId);
        AttrResponseVo attrResponseVo = new AttrResponseVo();
        BeanUtils.copyProperties(attrEntity,attrResponseVo);

        if(attrEntity.getAttrType().equals(ProductConstant.AttrTypeEnum.ATTR_TYPE_BASE.getCode())){
            AttrAttrgroupRelationEntity entity = attrAttrgroupRelationService.getOne(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id",attrEntity.getAttrId()));
            if(entity != null){
                attrResponseVo.setAttrGroupId(entity.getAttrGroupId());
            }
        }

        List<Long> path = categoryService.queryFullCategoryPath(attrEntity.getCatelogId());
        attrResponseVo.setCatelogPath(path);

        return attrResponseVo;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateAttr(AttrVo attrVo) {
        AttrEntity attrEntity = new AttrEntity();
        BeanUtils.copyProperties(attrVo,attrEntity);
        this.updateById(attrEntity);

        if(attrEntity.getAttrType().equals(ProductConstant.AttrTypeEnum.ATTR_TYPE_BASE.getCode()) && attrVo.getAttrGroupId() != null){
            AttrAttrgroupRelationEntity relation = attrAttrgroupRelationService.getOne(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attrVo.getAttrId()));
            if(relation == null){
                AttrAttrgroupRelationEntity entity = new AttrAttrgroupRelationEntity();
                entity.setAttrId(attrVo.getAttrId());
                entity.setAttrGroupId(attrVo.getAttrGroupId());
                attrAttrgroupRelationService.save(entity);
            }else{
                relation.setAttrGroupId(attrVo.getAttrGroupId());
                attrAttrgroupRelationService.updateById(relation);
            }
//            AttrAttrgroupRelationEntity entity = new AttrAttrgroupRelationEntity();
//            entity.setAttrId(attrVo.getAttrId());
//            entity.setAttrGroupId(attrVo.getAttrGroupId());
//            attrAttrgroupRelationService.update(entity,new UpdateWrapper<AttrAttrgroupRelationEntity>().eq("attr_id",attrVo.getAttrId()));
        }
    }

    @Override
    public List<AttrEntity> getRelationAttr(Long attrGroupId) {
        List<AttrAttrgroupRelationEntity> relationList = attrAttrgroupRelationService.list(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_group_id", attrGroupId));
        if(CollectionUtils.isEmpty(relationList)){
            return null;
        }
        return this.listByIds(relationList.stream().map(AttrAttrgroupRelationEntity::getAttrId).collect(Collectors.toList()));
    }

    @Override
    public PageUtils getNoattrRelation(Long attrGroupId,Map<String, Object> params) {
        //当前分类下的 未被关联的规格参数
        AttrGroupEntity attrGroupEntity = attrGroupService.getById(attrGroupId);
        //分类id
        Long catelogId = attrGroupEntity.getCatelogId();
        //当前分类下的所有分组
        List<AttrGroupEntity> attrGroupEntities = attrGroupService.list(new QueryWrapper<AttrGroupEntity>().eq("catelog_id", catelogId));
        List<Long> attrGroupIdList = attrGroupEntities.stream().map(AttrGroupEntity::getAttrGroupId).collect(Collectors.toList());
        //所有分组下被关联的规格参数
        List<AttrAttrgroupRelationEntity> relations = attrAttrgroupRelationService.list(new QueryWrapper<AttrAttrgroupRelationEntity>().in("attr_group_id", attrGroupIdList));
        List<Long> attrIdList = relations.stream().map(AttrAttrgroupRelationEntity::getAttrId).collect(Collectors.toList());
        //过滤掉已被关联的规格参数
        QueryWrapper<AttrEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("attr_type", ProductConstant.AttrTypeEnum.ATTR_TYPE_BASE.getCode());
        if(!CollectionUtils.isEmpty(attrIdList)){
            queryWrapper.notIn("attr_id", attrIdList);
        }
        String key = (String) params.get("key");
        if(StringUtils.isNotBlank(key)){
            queryWrapper.and(obj ->{
                obj.eq("attr_id",key).or().like("attr_name",key);
            });
        }

        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                queryWrapper
        );

        return new PageUtils(page);
    }

    @Override
    public List<Long> selectSearchAttrs(List<Long> attrIds) {
        return attrDao.selectSearchAttrs(attrIds);
    }
}
