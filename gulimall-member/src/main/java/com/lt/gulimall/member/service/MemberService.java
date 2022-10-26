package com.lt.gulimall.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lt.gulimall.common.utils.PageUtils;
import com.lt.gulimall.member.entity.MemberEntity;

import java.util.Map;

/**
 * 会员
 *
 * @author lt
 * @email 1746397845@qq.com
 * @date 2022-10-26 12:25:17
 */
public interface MemberService extends IService<MemberEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

