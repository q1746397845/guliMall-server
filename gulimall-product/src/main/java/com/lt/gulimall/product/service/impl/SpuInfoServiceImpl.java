package com.lt.gulimall.product.service.impl;

import com.lt.gulimall.common.to.SkuReductionTo;
import com.lt.gulimall.common.to.SpuBoundsTo;
import com.lt.gulimall.common.utils.R;
import com.lt.gulimall.product.entity.*;
import com.lt.gulimall.product.feign.CouponFeignService;
import com.lt.gulimall.product.query.SpuQuery;
import com.lt.gulimall.product.service.*;
import com.lt.gulimall.product.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lt.gulimall.common.utils.PageUtils;
import com.lt.gulimall.common.utils.Query;

import com.lt.gulimall.product.dao.SpuInfoDao;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;


@Service("spuInfoService")
@Slf4j
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoDao, SpuInfoEntity> implements SpuInfoService {

    @Resource
    private SpuImagesService spuImagesService;
    @Resource
    private SpuInfoDescService spuInfoDescService;
    @Resource
    private SkuInfoService skuInfoService;
    @Resource
    private SkuSaleAttrValueService skuSaleAttrValueService;
    @Resource
    private SkuImagesService skuImagesService;
    @Resource
    private ProductAttrValueService productAttrValueService;
    @Resource
    private AttrService attrService;
    @Resource
    private CouponFeignService couponFeignService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                new QueryWrapper<SpuInfoEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils querybByCondition(Map<String, Object> params) {
        QueryWrapper<SpuInfoEntity> queryWrapper = new QueryWrapper<>();
        String key = (String)params.get("key");
        if(!StringUtils.isEmpty(key)){
            queryWrapper.and((w) ->{
                w.eq("id",key).or().like("spu_name",key);
            });
        }
        String status = (String)params.get("status");
        if(!StringUtils.isEmpty(status)){
            queryWrapper.eq("publish_status",status);
        }
        String brandId = (String)params.get("brandId");
        if(!StringUtils.isEmpty(brandId) && !brandId.equals("0")){
            queryWrapper.eq("brand_id",brandId);
        }
        String catelogId = (String)params.get("catelogId");
        if(!StringUtils.isEmpty(catelogId) && !catelogId.equals("0")){
            queryWrapper.eq("catalog_id",catelogId);
        }
        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params), queryWrapper
        );

        return new PageUtils(page);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveInfo(SpuSaveVo spuSaveVo) {
        //1.入库 pms_spu_info表
        SpuInfoEntity spuInfoEntity = new SpuInfoEntity();
        BeanUtils.copyProperties(spuSaveVo,spuInfoEntity);
        spuInfoEntity.setCreateTime(new Date());
        spuInfoEntity.setUpdateTime(new Date());
        this.save(spuInfoEntity);
        //2.入库 pms_spu_info_desc表
        SpuInfoDescEntity spuInfoDescEntity = new SpuInfoDescEntity();
        spuInfoDescEntity.setSpuId(spuInfoEntity.getId());
        spuInfoDescEntity.setDecript(String.join(",",spuSaveVo.getDecript()));
        spuInfoDescService.save(spuInfoDescEntity);

        //3.入库 pms_spu_images表
        spuImagesService.saveImages(spuInfoEntity.getId(),spuSaveVo.getImages());

        //4.入库pms_product_attr_value表
        List<ProductAttrValueEntity> productAttrValueEntities = spuSaveVo.getBaseAttrs().stream().map(baseAttrs -> {
            ProductAttrValueEntity productAttrValueEntity = new ProductAttrValueEntity();
            productAttrValueEntity.setAttrId(baseAttrs.getAttrId());
            productAttrValueEntity.setSpuId(spuInfoEntity.getId());
            productAttrValueEntity.setAttrValue(baseAttrs.getAttrValues());
            AttrEntity byId = attrService.getById(baseAttrs.getAttrId());
            productAttrValueEntity.setAttrName(byId.getAttrName());
            productAttrValueEntity.setQuickShow(byId.getShowDesc());
            return productAttrValueEntity;
        }).collect(Collectors.toList());
        productAttrValueService.saveBatch(productAttrValueEntities);

        //5.入库商品积分信息
        SpuBoundsTo spuBoundsTo = new SpuBoundsTo();
        spuBoundsTo.setSpuId(spuInfoEntity.getId());
        spuBoundsTo.setBuyBounds(spuSaveVo.getBounds().getBuyBounds());
        spuBoundsTo.setGrowBounds(spuSaveVo.getBounds().getGrowBounds());
        R r = couponFeignService.saveBounds(spuBoundsTo);
        if(r.getCode() != 0){
            log.error("入库商品积分信息失败",r.getMsg());
        }

        //6.入库 sku信息
        List<Skus> skus = spuSaveVo.getSkus();
        if (!CollectionUtils.isEmpty(skus)) {
            skus.forEach(sku ->{
                //6.1入库 pms_sku_info 表

                List<Images> defaultImages = sku.getImages().stream().filter(images -> images.getDefaultImg() == 1).collect(Collectors.toList());
                SkuInfoEntity skuInfoEntity = new SkuInfoEntity();
                BeanUtils.copyProperties(sku,skuInfoEntity);
                skuInfoEntity.setSpuId(spuInfoEntity.getId());
                skuInfoEntity.setCatalogId(spuInfoEntity.getCatalogId());
                skuInfoEntity.setBrandId(spuInfoEntity.getBrandId());
                if (!CollectionUtils.isEmpty(defaultImages)) {
                    Images images = defaultImages.get(0);
                    skuInfoEntity.setSkuDefaultImg(images.getImgUrl());
                }
                skuInfoService.save(skuInfoEntity);
                //6.2入库pms_sku_images表
                List<SkuImagesEntity> skuImagesEntityList = sku.getImages().stream()
                        .filter(image -> !StringUtils.isEmpty(image.getImgUrl()))
                        .map(image -> {
                            SkuImagesEntity skuImagesEntity = new SkuImagesEntity();
                            skuImagesEntity.setImgUrl(image.getImgUrl());
                            skuImagesEntity.setDefaultImg(image.getDefaultImg());
                            skuImagesEntity.setSkuId(skuInfoEntity.getSkuId());
                            return skuImagesEntity;
                        }).collect(Collectors.toList());
                skuImagesService.saveBatch(skuImagesEntityList);

                //6.3入库pms_sku_sale_attr_value 表
                List<Attr> attrs = sku.getAttr();
                if (CollectionUtils.isEmpty(attrs)) {
                    List<SkuSaleAttrValueEntity> skuSaleAttrValueEntities = attrs.stream().map(attr -> {
                        SkuSaleAttrValueEntity skuSaleAttrValueEntity = new SkuSaleAttrValueEntity();
                        skuSaleAttrValueEntity.setAttrId(attr.getAttrId());
                        skuSaleAttrValueEntity.setAttrName(attr.getAttrName());
                        skuSaleAttrValueEntity.setAttrValue(attr.getAttrValue());
                        skuSaleAttrValueEntity.setSkuId(skuInfoEntity.getSkuId());
                        return skuSaleAttrValueEntity;
                    }).collect(Collectors.toList());
                    skuSaleAttrValueService.saveBatch(skuSaleAttrValueEntities);
                }
                //6.4入库sku的优惠满减信息
                if(sku.getFullCount() > 0 || sku.getFullPrice().compareTo(new BigDecimal(0)) == 1){
                    SkuReductionTo skuReductionTo = new SkuReductionTo();
                    BeanUtils.copyProperties(sku,skuReductionTo);
                    skuReductionTo.setSkuId(skuInfoEntity.getSkuId());
                    R r1 = couponFeignService.saveSkuReduction(skuReductionTo);
                    if(r1.getCode() != 0){
                        log.error("sku的优惠满减信息失败",r1.getMsg());
                    }
                }
            });
        }

    }
}
