package com.lt.gulimall.coupon.service.impl;

import com.lt.gulimall.common.to.MemberPrice;
import com.lt.gulimall.common.to.SkuReductionTo;
import com.lt.gulimall.coupon.entity.MemberPriceEntity;
import com.lt.gulimall.coupon.entity.SkuLadderEntity;
import com.lt.gulimall.coupon.service.MemberPriceService;
import com.lt.gulimall.coupon.service.SkuLadderService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lt.gulimall.common.utils.PageUtils;
import com.lt.gulimall.common.utils.Query;

import com.lt.gulimall.coupon.dao.SkuFullReductionDao;
import com.lt.gulimall.coupon.entity.SkuFullReductionEntity;
import com.lt.gulimall.coupon.service.SkuFullReductionService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;


@Service("skuFullReductionService")
public class SkuFullReductionServiceImpl extends ServiceImpl<SkuFullReductionDao, SkuFullReductionEntity> implements SkuFullReductionService {

    @Autowired
    private SkuLadderService skuLadderService;
    @Autowired
    private MemberPriceService memberPriceService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuFullReductionEntity> page = this.page(
                new Query<SkuFullReductionEntity>().getPage(params),
                new QueryWrapper<SkuFullReductionEntity>()
        );

        return new PageUtils(page);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveSkuReduction(SkuReductionTo skuReductionTo) {
        if (skuReductionTo.getFullCount() > 0 ){
            SkuLadderEntity skuLadderEntity = new SkuLadderEntity();
            skuLadderEntity.setSkuId(skuReductionTo.getSkuId());
            skuLadderEntity.setAddOther(skuReductionTo.getCountStatus());
            skuLadderEntity.setDiscount(skuReductionTo.getDiscount());
            skuLadderEntity.setFullCount(skuReductionTo.getFullCount());
            skuLadderService.save(skuLadderEntity);
        }

        if(skuReductionTo.getFullPrice().compareTo(new BigDecimal(0)) == 1){
            SkuFullReductionEntity skuFullReductionEntity = new SkuFullReductionEntity();
            skuFullReductionEntity.setSkuId(skuReductionTo.getSkuId());
            skuFullReductionEntity.setAddOther(skuReductionTo.getCountStatus());
            skuFullReductionEntity.setFullPrice(skuReductionTo.getFullPrice());
            skuFullReductionEntity.setReducePrice(skuReductionTo.getReducePrice());
            this.save(skuFullReductionEntity);
        }

        List<MemberPrice> memberPrices = skuReductionTo.getMemberPrice();
        if (!CollectionUtils.isEmpty(memberPrices)) {
            List<MemberPriceEntity> collect = memberPrices.stream()
                    .filter(memberPrice -> memberPrice.getPrice().compareTo(new BigDecimal(0)) == 1)
                    .map(memberPrice -> {
                        MemberPriceEntity memberPriceEntity = new MemberPriceEntity();
                        memberPriceEntity.setSkuId(skuReductionTo.getSkuId());
                        memberPriceEntity.setAddOther(skuReductionTo.getCountStatus());
                        memberPriceEntity.setMemberLevelId(memberPrice.getId());
                        memberPriceEntity.setMemberLevelName(memberPrice.getName());
                        memberPriceEntity.setMemberPrice(memberPrice.getPrice());
                        return memberPriceEntity;
                    })
                    .collect(Collectors.toList());
            memberPriceService.saveBatch(collect);
        }
    }
}
