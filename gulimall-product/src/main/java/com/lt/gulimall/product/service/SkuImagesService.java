package com.lt.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lt.gulimall.common.utils.PageUtils;
import com.lt.gulimall.product.entity.SkuImagesEntity;

import java.util.Map;

/**
 * sku图片
 *
 * @author lt
 * @email 1746397845@qq.com
 * @date 2022-10-26 10:27:47
 */
public interface SkuImagesService extends IService<SkuImagesEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

