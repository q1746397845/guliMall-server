package com.lt.gulimall.order.dao;

import com.lt.gulimall.order.entity.OrderEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单
 * 
 * @author lt
 * @email 1746397845@qq.com
 * @date 2022-10-26 12:22:15
 */
@Mapper
public interface OrderDao extends BaseMapper<OrderEntity> {
	
}
