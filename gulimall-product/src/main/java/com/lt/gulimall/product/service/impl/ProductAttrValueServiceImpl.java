package com.lt.gulimall.product.service.impl;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lt.gulimall.common.utils.PageUtils;
import com.lt.gulimall.common.utils.Query;

import com.lt.gulimall.product.dao.ProductAttrValueDao;
import com.lt.gulimall.product.entity.ProductAttrValueEntity;
import com.lt.gulimall.product.service.ProductAttrValueService;


@Service("productAttrValueService")
public class ProductAttrValueServiceImpl extends ServiceImpl<ProductAttrValueDao, ProductAttrValueEntity> implements ProductAttrValueService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ProductAttrValueEntity> page = this.page(
                new Query<ProductAttrValueEntity>().getPage(params),
                new QueryWrapper<ProductAttrValueEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<ProductAttrValueEntity> getBaseAttrList(Long supId) {
        QueryWrapper<ProductAttrValueEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("spu_id",supId);
        List<ProductAttrValueEntity> list = this.list(queryWrapper);
        return list;
    }


    @Override
    public void updateBaseAttr(Long spuId, List<ProductAttrValueEntity> productAttrValueEntities) {
        this.remove(new QueryWrapper<ProductAttrValueEntity>().eq("spu_id",spuId));
        productAttrValueEntities.forEach(item ->{
            item.setSpuId(spuId);
        });
        this.saveBatch(productAttrValueEntities);
    }

}
