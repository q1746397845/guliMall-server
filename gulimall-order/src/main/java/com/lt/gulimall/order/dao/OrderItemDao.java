package com.lt.gulimall.order.dao;

import com.lt.gulimall.order.entity.OrderItemEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单项信息
 * 
 * @author lt
 * @email 1746397845@qq.com
 * @date 2022-10-26 12:22:15
 */
@Mapper
public interface OrderItemDao extends BaseMapper<OrderItemEntity> {
	
}
