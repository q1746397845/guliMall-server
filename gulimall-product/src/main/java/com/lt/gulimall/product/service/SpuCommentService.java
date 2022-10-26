package com.lt.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lt.gulimall.common.utils.PageUtils;
import com.lt.gulimall.product.entity.SpuCommentEntity;

import java.util.Map;

/**
 * 商品评价
 *
 * @author lt
 * @email 1746397845@qq.com
 * @date 2022-10-26 10:27:47
 */
public interface SpuCommentService extends IService<SpuCommentEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

