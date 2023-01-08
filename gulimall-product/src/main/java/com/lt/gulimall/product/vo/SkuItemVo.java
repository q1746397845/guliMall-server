package com.lt.gulimall.product.vo;

import com.lt.gulimall.product.entity.SkuImagesEntity;
import com.lt.gulimall.product.entity.SkuInfoEntity;
import com.lt.gulimall.product.entity.SpuInfoDescEntity;
import lombok.Data;

import java.util.List;

/**
 * @ClassName SkuItemVo
 * @Description:
 * @Author lite
 * @Date 2023/1/6
 * @Version V1.0
 **/
@Data
public class SkuItemVo {

    /**
     * sku基本信息
     */
    private SkuInfoEntity info;
    /**
     * sku图片信息
     */
    private List<SkuImagesEntity> images;
    /**
     * spu销售属性组合
     */
    private List<SkuItemSaleAttrVo> saleAttr;
    /**
     * spu介绍
     */
    private SpuInfoDescEntity desc;
    /**
     * spu规格参数
     */
    private List<SkuItemGroupAttrVo> groupAttrs;


}
