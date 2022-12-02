package com.lt.gulimall.ware.service.impl;

import com.lt.gulimall.common.constant.WareConstant;
import com.lt.gulimall.ware.entity.PurchaseDetailEntity;
import com.lt.gulimall.ware.service.PurchaseDetailService;
import com.lt.gulimall.ware.service.WareSkuService;
import com.lt.gulimall.ware.vo.MergeVo;
import com.lt.gulimall.ware.vo.PurchaseDoneVo;
import com.lt.gulimall.ware.vo.PurchaseItemDoneVo;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lt.gulimall.common.utils.PageUtils;
import com.lt.gulimall.common.utils.Query;

import com.lt.gulimall.ware.dao.PurchaseDao;
import com.lt.gulimall.ware.entity.PurchaseEntity;
import com.lt.gulimall.ware.service.PurchaseService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;


@Service("purchaseService")
public class PurchaseServiceImpl extends ServiceImpl<PurchaseDao, PurchaseEntity> implements PurchaseService {

    @Resource
    private PurchaseDetailService purchaseDetailService;

    @Resource
    private WareSkuService wareSkuService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        QueryWrapper<PurchaseEntity> queryWrapper = new QueryWrapper<>();
        String key = (String)params.get("key");
        if(!StringUtils.isEmpty(key)){
            queryWrapper.like("assignee_name",key).or().like("phone",key);
        }
        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),queryWrapper
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils unreceiveList() {
        Map<String, Object> map = new HashMap<>();
        QueryWrapper<PurchaseEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", WareConstant.PurchaseStatus.CREATED.getCode()).or().eq("status",WareConstant.PurchaseStatus.ASSIGNED.getCode());
        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(map),
                queryWrapper
        );
        return new PageUtils(page);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void merge(MergeVo mergeVo) {
        if(mergeVo.getPurchaseId() == null){
            //新建采购单
            PurchaseEntity purchaseEntity = new PurchaseEntity();
            purchaseEntity.setStatus(WareConstant.PurchaseStatus.CREATED.getCode());
            purchaseEntity.setCreateTime(new Date());
            purchaseEntity.setUpdateTime(new Date());
            this.save(purchaseEntity);
            mergeVo.setPurchaseId(purchaseEntity.getId());
        }
        List<Long> items = mergeVo.getItems();
        List<PurchaseDetailEntity> collect = items.stream().map(detailId -> {
            PurchaseDetailEntity purchaseDetailEntity = new PurchaseDetailEntity();
            purchaseDetailEntity.setId(detailId);
            purchaseDetailEntity.setPurchaseId(mergeVo.getPurchaseId());
            purchaseDetailEntity.setStatus(WareConstant.PurchaseDetailStatus.ASSIGNED.getCode());
            return purchaseDetailEntity;
        }).collect(Collectors.toList());
        purchaseDetailService.updateBatchById(collect);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void received(List<Long> purchaseIds) {
        if(CollectionUtils.isEmpty(purchaseIds)){
            return;
        }
        //修改采购单状态
        List<PurchaseEntity> collect = purchaseIds.stream().map(purchaseId -> this.getById(purchaseId)).filter(item ->{
            if(item.getStatus().equals(WareConstant.PurchaseStatus.CREATED.getCode())
                    || item.getStatus().equals(WareConstant.PurchaseStatus.ASSIGNED.getCode())){
                return true;
            }else{
                return false;
            }
        }).map(item ->{
            item.setStatus(WareConstant.PurchaseStatus.RECEIVE.getCode());
            item.setUpdateTime(new Date());
            return item;
        }).collect(Collectors.toList());
        this.updateBatchById(collect);
        //修改采购需求状态
        collect.forEach(item ->{
            List<PurchaseDetailEntity> purchaseDetailEntities = purchaseDetailService.queryListByPurchaseId(item.getId());
            List<PurchaseDetailEntity> collect1 = purchaseDetailEntities.stream().map(detail -> {
                detail.setStatus(WareConstant.PurchaseDetailStatus.BUYING.getCode());
                return detail;
            }).collect(Collectors.toList());
            purchaseDetailService.updateBatchById(collect1);
        });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void donePurchase(PurchaseDoneVo purchaseDoneVo) {

        AtomicReference<Boolean> flag = new AtomicReference<>(true);
        List<PurchaseItemDoneVo> items = purchaseDoneVo.getItems();
        List<PurchaseDetailEntity> collect = items.stream().map(item -> {
            PurchaseDetailEntity purchaseDetailEntity = new PurchaseDetailEntity();
            purchaseDetailEntity.setId(item.getItemId());
            purchaseDetailEntity.setStatus(item.getStatus());
            if(item.getStatus().equals(WareConstant.PurchaseDetailStatus.PURCHASE_ERROR.getCode())){
                flag.set(false);
            }else {
                //成功新增库存
                PurchaseDetailEntity byId = purchaseDetailService.getById(item.getItemId());
                wareSkuService.addStock(byId.getSkuId(),byId.getWareId(),byId.getSkuNum());
            }
            return purchaseDetailEntity;
        }).collect(Collectors.toList());
        purchaseDetailService.updateBatchById(collect);

        PurchaseEntity purchaseEntity = new PurchaseEntity();
        purchaseEntity.setId(purchaseDoneVo.getId());
        purchaseEntity.setUpdateTime(new Date());
        purchaseEntity.setStatus(flag.get() ? WareConstant.PurchaseStatus.FINISH.getCode() : WareConstant.PurchaseStatus.HAS_ERROR.getCode());
        this.updateById(purchaseEntity);
    }
}
