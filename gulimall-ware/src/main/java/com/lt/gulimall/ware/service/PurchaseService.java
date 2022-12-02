package com.lt.gulimall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lt.gulimall.common.utils.PageUtils;
import com.lt.gulimall.ware.entity.PurchaseEntity;
import com.lt.gulimall.ware.vo.MergeVo;
import com.lt.gulimall.ware.vo.PurchaseDoneVo;

import java.util.List;
import java.util.Map;

/**
 * 采购信息
 *
 * @author lt
 * @email 1746397845@qq.com
 * @date 2022-10-26 12:20:23
 */
public interface PurchaseService extends IService<PurchaseEntity> {

    PageUtils queryPage(Map<String, Object> params);

    PageUtils unreceiveList();

    void merge(MergeVo mergeVo);

    void received(List<Long> purchaseIds);

    void donePurchase(PurchaseDoneVo purchaseDoneVo);
}

