package com.lt.gulimall.product.service.impl;

import com.alibaba.fastjson.JSON;
import com.lt.gulimall.product.entity.AttrEntity;
import com.lt.gulimall.product.service.AttrService;
import com.lt.gulimall.product.vo.AttrGroupWithAttrsVo;
import com.lt.gulimall.product.vo.AttrVo;
import org.apache.commons.lang.StringUtils;
import org.bouncycastle.jcajce.provider.symmetric.ARC4;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lt.gulimall.common.utils.PageUtils;
import com.lt.gulimall.common.utils.Query;

import com.lt.gulimall.product.dao.AttrGroupDao;
import com.lt.gulimall.product.entity.AttrGroupEntity;
import com.lt.gulimall.product.service.AttrGroupService;

import javax.annotation.Resource;


@Service("attrGroupService")
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService {

    @Resource
    private AttrService attrService;

    @Override
    public List<AttrGroupWithAttrsVo> getAttrGroupWithAttrs(Long catelogId) {
        //首先获取当前分类下的属性分组
        List<AttrGroupEntity> attrGroupEntities = this.list(new QueryWrapper<AttrGroupEntity>().eq("catelog_id", catelogId));
        return attrGroupEntities.stream().map(attrGroup ->{
            AttrGroupWithAttrsVo attrGroupWithAttrsVo = new AttrGroupWithAttrsVo();
            BeanUtils.copyProperties(attrGroup,attrGroupWithAttrsVo);
            List<AttrEntity> relationAttr = attrService.getRelationAttr(attrGroup.getAttrGroupId());
            attrGroupWithAttrsVo.setAttrs(JSON.parseArray(JSON.toJSONString(relationAttr), AttrVo.class));
            return attrGroupWithAttrsVo;
        }).collect(Collectors.toList());
    };

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrGroupEntity> page = this.page(
                new Query<AttrGroupEntity>().getPage(params),
                new QueryWrapper<AttrGroupEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPageByParams(Map<String, Object> params, Long catelogId) {
        QueryWrapper<AttrGroupEntity> queryWrapper = new QueryWrapper<>();
        if(catelogId != 0){
            queryWrapper.eq("catelog_id",catelogId);
        }
        String key = (String) params.get("key");
        if(StringUtils.isNotBlank(key)){
            queryWrapper.and(obj ->{
                obj.eq("attr_group_id",key).or().like("attr_group_name",key);
            });
        }
        IPage<AttrGroupEntity> page = this.page(
                new Query<AttrGroupEntity>().getPage(params),queryWrapper
        );
        return new PageUtils(page);
    }
}
