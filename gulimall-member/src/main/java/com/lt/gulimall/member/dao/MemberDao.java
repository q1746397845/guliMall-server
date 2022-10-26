package com.lt.gulimall.member.dao;

import com.lt.gulimall.member.entity.MemberEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员
 * 
 * @author lt
 * @email 1746397845@qq.com
 * @date 2022-10-26 12:25:17
 */
@Mapper
public interface MemberDao extends BaseMapper<MemberEntity> {
	
}
