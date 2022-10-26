package com.lt.gulimall.coupon.dao;

import com.lt.gulimall.coupon.entity.CouponEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 优惠券信息
 * 
 * @author lt
 * @email 1746397845@qq.com
 * @date 2022-10-26 12:27:30
 */
@Mapper
public interface CouponDao extends BaseMapper<CouponEntity> {
	
}
