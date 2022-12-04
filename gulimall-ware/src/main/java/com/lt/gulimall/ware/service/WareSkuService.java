package com.lt.gulimall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lt.gulimall.common.to.SkuHasStockTo;
import com.lt.gulimall.common.utils.PageUtils;
import com.lt.gulimall.ware.entity.WareSkuEntity;

import java.util.List;
import java.util.Map;

/**
 * 商品库存
 *
 * @author lt
 * @email 1746397845@qq.com
 * @date 2022-10-26 12:20:23
 */
public interface WareSkuService extends IService<WareSkuEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void addStock(Long skuId, Long wareId, Integer skuNum);

    List<SkuHasStockTo> getSkuStock(List<Long> skuIds);
}

