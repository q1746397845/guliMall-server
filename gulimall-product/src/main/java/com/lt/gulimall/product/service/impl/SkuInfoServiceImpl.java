package com.lt.gulimall.product.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lt.gulimall.common.utils.PageUtils;
import com.lt.gulimall.common.utils.Query;

import com.lt.gulimall.product.dao.SkuInfoDao;
import com.lt.gulimall.product.entity.SkuInfoEntity;
import com.lt.gulimall.product.service.SkuInfoService;
import org.springframework.util.StringUtils;


@Service("skuInfoService")
public class SkuInfoServiceImpl extends ServiceImpl<SkuInfoDao, SkuInfoEntity> implements SkuInfoService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuInfoEntity> page = this.page(
                new Query<SkuInfoEntity>().getPage(params),
                new QueryWrapper<SkuInfoEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryByCondition(Map<String, Object> params) {
        QueryWrapper<SkuInfoEntity> queryWrapper = new QueryWrapper<>();
        String key = (String)params.get("key");
        if(!StringUtils.isEmpty(key)){
            queryWrapper.and((w) ->{
                w.eq("sku_id",key).or().like("sku_name",key);
            });
        }
        String brandId = (String)params.get("brandId");
        if(!StringUtils.isEmpty(brandId) && !brandId.equals("0")){
            queryWrapper.eq("brand_id",brandId);
        }
        String catelogId = (String)params.get("catelogId");
        if(!StringUtils.isEmpty(catelogId) && !catelogId.equals("0")){
            queryWrapper.eq("catalog_id",catelogId);
        }
        String min = (String)params.get("min");
        if(!StringUtils.isEmpty(min)){
            queryWrapper.ge("price",min);
        }
        String max = (String)params.get("max");
        if(!StringUtils.isEmpty(max) && !max.equals("0")){
            queryWrapper.le("price",max);
        }
        IPage<SkuInfoEntity> page = this.page(
                new Query<SkuInfoEntity>().getPage(params),queryWrapper);

        return new PageUtils(page);
    }
}
