package com.lt.gulimall.ware.service.impl;

import com.lt.gulimall.common.utils.R;
import com.lt.gulimall.ware.feign.ProductFeignService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lt.gulimall.common.utils.PageUtils;
import com.lt.gulimall.common.utils.Query;

import com.lt.gulimall.ware.dao.WareSkuDao;
import com.lt.gulimall.ware.entity.WareSkuEntity;
import com.lt.gulimall.ware.service.WareSkuService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;


@Service("wareSkuService")
public class WareSkuServiceImpl extends ServiceImpl<WareSkuDao, WareSkuEntity> implements WareSkuService {

    @Resource
    private ProductFeignService productFeignService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        QueryWrapper<WareSkuEntity> queryWrapper = new QueryWrapper<>();
        String skuId = (String)params.get("skuId");
        String wareId = (String)params.get("wareId");
        if(!StringUtils.isEmpty(skuId)){
            queryWrapper.eq("sku_id",skuId);
        }
        if(!StringUtils.isEmpty(wareId)){
            queryWrapper.eq("ware_id",wareId);
        }

        IPage<WareSkuEntity> page = this.page(
                new Query<WareSkuEntity>().getPage(params), queryWrapper
        );

        return new PageUtils(page);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addStock(Long skuId, Long wareId, Integer skuNum) {
        //判断当前仓库是否有当前商品
        QueryWrapper<WareSkuEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("sku_id",skuId).eq("ware_id",wareId);
        WareSkuEntity one = this.getOne(queryWrapper);
        if(one == null){
            one = new WareSkuEntity();
            one.setSkuId(skuId);
            one.setWareId(wareId);
            one.setStock(skuNum);
            one.setStockLocked(0);
            R r = productFeignService.getSkuInfo(skuId);
            if(r.get("code").equals("0")){
                Map<String,Object> skuInfo = (Map<String, Object>) r.get("skuInfo");
                String skuName = (String)skuInfo.get("skuName");
                one.setSkuName(skuName);
            }
            this.save(one);
        }else{
            //新增库存
            one.setStock(one.getStock() + skuNum);
            this.updateById(one);
        }
    }
}
