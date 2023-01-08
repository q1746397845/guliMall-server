package com.lt.gulimall.product.service.impl;

import com.lt.gulimall.product.config.MyThreadConfig;
import com.lt.gulimall.product.entity.SkuImagesEntity;
import com.lt.gulimall.product.entity.SpuInfoDescEntity;
import com.lt.gulimall.product.service.*;
import com.lt.gulimall.product.vo.SkuItemGroupAttrVo;
import com.lt.gulimall.product.vo.SkuItemSaleAttrVo;
import com.lt.gulimall.product.vo.SkuItemVo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lt.gulimall.common.utils.PageUtils;
import com.lt.gulimall.common.utils.Query;

import com.lt.gulimall.product.dao.SkuInfoDao;
import com.lt.gulimall.product.entity.SkuInfoEntity;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;


@Service("skuInfoService")
public class SkuInfoServiceImpl extends ServiceImpl<SkuInfoDao, SkuInfoEntity> implements SkuInfoService {

    @Resource
    private SpuInfoDescService spuInfoDescService;

    @Resource
    private SkuImagesService skuImagesService;

    @Resource
    private AttrGroupService attrGroupService;

    @Resource
    private SkuSaleAttrValueService skuSaleAttrValueService;

    @Resource
    private ThreadPoolExecutor executor;

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

    @Override
    public SkuItemVo skuItem(Long skuId) throws ExecutionException, InterruptedException {
        SkuItemVo skuItemVo = new SkuItemVo();

        CompletableFuture<SkuInfoEntity> infoFuture = CompletableFuture.supplyAsync(() -> {
            //sku详情
            SkuInfoEntity byId = this.getById(skuId);
            skuItemVo.setInfo(byId);
            return byId;
        }, executor);

        CompletableFuture<Void> descFuture = infoFuture.thenAcceptAsync((res) -> {
            //spu描述
            SpuInfoDescEntity desc = spuInfoDescService.getBySpuId(res.getSpuId());
            skuItemVo.setDesc(desc);
        }, executor);

        CompletableFuture<Void> groupFuture = infoFuture.thenAcceptAsync((res) -> {
            ///spu规格参数
            List<SkuItemGroupAttrVo> groupAttrs = attrGroupService.getAttrGroupWithAttrsBySpuId(res.getSpuId());
            skuItemVo.setGroupAttrs(groupAttrs);
        }, executor);

        CompletableFuture<Void> saleAttrFuture = infoFuture.thenAcceptAsync((res) -> {
            //spu销售属性
            List<SkuItemSaleAttrVo> saleAttr = skuSaleAttrValueService.getSaleAttrsBySpuId(res.getSpuId());
            skuItemVo.setSaleAttr(saleAttr);
        }, executor);


        CompletableFuture<Void> imagesFuture = CompletableFuture.runAsync(() -> {
            //sku图片
            List<SkuImagesEntity> images = skuImagesService.getBySkuId(skuId);
            skuItemVo.setImages(images);
        }, executor);

        CompletableFuture.allOf(descFuture,groupFuture,saleAttrFuture,imagesFuture).get();
        return skuItemVo;
    }

//    @Override
//    public SkuItemVo skuItem(Long skuId) {
//        SkuItemVo skuItemVo = new SkuItemVo();
//
//        //sku详情
//        SkuInfoEntity byId = this.getById(skuId);
//        skuItemVo.setInfo(byId);
//
//        //spu描述
//        SpuInfoDescEntity desc = spuInfoDescService.getBySpuId(byId.getSpuId());
//        skuItemVo.setDesc(desc);
//        //sku图片
//        List<SkuImagesEntity> images = skuImagesService.getBySkuId(skuId);
//        skuItemVo.setImages(images);
//        //spu规格参数
//        List<SkuItemGroupAttrVo> groupAttrs = attrGroupService.getAttrGroupWithAttrsBySpuId(byId.getSpuId());
//        skuItemVo.setGroupAttrs(groupAttrs);
//        //spu销售属性
//        List<SkuItemSaleAttrVo> saleAttr = skuSaleAttrValueService.getSaleAttrsBySpuId(byId.getSpuId());
//        skuItemVo.setSaleAttr(saleAttr);
//        return skuItemVo;
//    }
}
