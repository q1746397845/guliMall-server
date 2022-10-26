package com.lt.gulimall.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lt.gulimall.common.utils.PageUtils;
import com.lt.gulimall.member.entity.IntegrationChangeHistoryEntity;

import java.util.Map;

/**
 * 积分变化历史记录
 *
 * @author lt
 * @email 1746397845@qq.com
 * @date 2022-10-26 12:25:17
 */
public interface IntegrationChangeHistoryService extends IService<IntegrationChangeHistoryEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

